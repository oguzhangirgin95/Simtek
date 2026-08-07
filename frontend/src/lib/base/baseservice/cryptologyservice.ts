import { Injectable } from '@angular/core';
import AES from 'crypto-js/aes';
import Utf8 from 'crypto-js/enc-utf8';
import { environment } from '../../../environments/environment';
import { BaseService } from './baseservice';

@Injectable({
  providedIn: 'root',
})
export class CryptologyService extends BaseService {
  private readonly key = Utf8.parse(environment.cryptoKey);

  private readonly iv = Utf8.parse(environment.cryptoKey.substring(0, 16));

  public encryption(value: string): string {
    if (!value) {
      return '';
    }

    return AES.encrypt(value, this.key, { iv: this.iv }).toString();
  }

  public decryption(value: string): string {
    if (!value) {
      return '';
    }

    try {
      return AES.decrypt(value, this.key, { iv: this.iv }).toString(Utf8);
    } catch {
      return '';
    }
  }
}
