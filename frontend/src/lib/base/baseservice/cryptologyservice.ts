import { Injectable } from '@angular/core';
import * as CryptoJS from 'crypto-js';
import { environment } from '../../../environments/environment';
import { BaseService } from './baseservice';

@Injectable({
  providedIn: 'root',
})
export class CryptologyService extends BaseService {
  private readonly key = CryptoJS.enc.Utf8.parse(environment.cryptoKey);

  private readonly iv = CryptoJS.enc.Utf8.parse(environment.cryptoKey.substring(0, 16));

  public encryption(value: string): string {
    if (!value) {
      return '';
    }

    return CryptoJS.AES.encrypt(value, this.key, { iv: this.iv }).toString();
  }

  public decryption(value: string): string {
    if (!value) {
      return '';
    }

    try {
      return CryptoJS.AES.decrypt(value, this.key, { iv: this.iv }).toString(CryptoJS.enc.Utf8);
    } catch {
      return '';
    }
  }
}
