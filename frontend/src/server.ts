import {
  AngularNodeAppEngine,
  createNodeRequestHandler,
  isMainModule,
  writeResponseToNodeResponse,
} from '@angular/ssr/node';
import express from 'express';
import { join } from 'node:path';

const browserDistFolder = join(import.meta.dirname, '../browser');

const apiAddress = process.env['API_URL'] ?? 'http://localhost:8080';

const apiUrl = apiAddress.startsWith('http') ? apiAddress : `http://${apiAddress}`;

const app = express();
const angularApp = new AngularNodeAppEngine({ trustProxyHeaders: true });

app.use('/api', express.raw({ type: () => true, limit: '10mb' }), (req, res) => {
  const headers: Record<string, string> = {};

  for (const [key, value] of Object.entries(req.headers)) {
    if (typeof value === 'string' && key !== 'host' && key !== 'connection' && key !== 'content-length') {
      headers[key] = value;
    }
  }

  const gonderilecek = req.body as Buffer | undefined;
  const body = req.method === 'GET' || req.method === 'HEAD' || !gonderilecek?.length
    ? undefined
    : new Uint8Array(gonderilecek);

  fetch(apiUrl + req.url, { method: req.method, headers, body })
    .then(async (response) => {
      res.status(response.status);

      response.headers.forEach((value, key) => {
        if (key !== 'content-encoding' && key !== 'transfer-encoding' && key !== 'content-length') {
          res.setHeader(key, value);
        }
      });

      res.send(Buffer.from(await response.arrayBuffer()));
    })
    .catch((error) => {
      console.error('API proxy:', error);
      res.status(502).json({ message: 'Servise ulasilamadi.' });
    });
});

app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  }),
);

app.use((req, res, next) => {
  angularApp
    .handle(req)
    .then((response) =>
      response ? writeResponseToNodeResponse(response, res) : next(),
    )
    .catch(next);
});

if (isMainModule(import.meta.url) || process.env['pm_id']) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, (error) => {
    if (error) {
      throw error;
    }

    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

export const reqHandler = createNodeRequestHandler(app);
