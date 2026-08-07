FROM node:22-alpine AS build
WORKDIR /build

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build:prod

FROM node:22-alpine
WORKDIR /app

COPY --from=build /build/dist ./dist

ENV PORT=4000
EXPOSE 4000

CMD ["node", "dist/frontend/server/server.mjs"]
