import { Injectable } from '@angular/core';
import { ValidationError, ValidationRuleConfig, ValidatorEnum } from '../baseconfig/config';
import { BaseService } from './baseservice';

@Injectable({
  providedIn: 'root',
})
export class Validationservice extends BaseService {
  /**
   * Step'in validation kurallarini calistirir, hatalari dondurur.
   * Deger state'ten okundugu icin okuma islemi getValue ile disaridan verilir.
   */
  public validate(rules: ValidationRuleConfig[] = [], getValue: (path: string) => any): ValidationError[] {
    const errors: ValidationError[] = [];

    for (const rule of rules) {
      const value = getValue(rule.id);
      if (!this.isValid(rule, value)) {
        errors.push({ id: rule.id, message: rule.validationMessage });
      }
    }

    return errors;
  }

  /** tek bir kurali kontrol eder */
  public isValid(rule: ValidationRuleConfig, value: any): boolean {
    // customValidation verildiyse kural odur
    if (rule.customValidation) {
      return rule.customValidation(value);
    }

    const text = value === null || value === undefined ? '' : String(value);

    switch (rule.validatorType) {
      case ValidatorEnum.Required:
        return text.trim() !== '';
      case ValidatorEnum.Regex:
        return !rule.regex || new RegExp(rule.regex).test(text);
      case ValidatorEnum.MinLength:
        return text.length >= (rule.minLength ?? 0);
      case ValidatorEnum.MaxLength:
        return text.length <= (rule.maxLength ?? text.length);
      case ValidatorEnum.Email:
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(text);
      case ValidatorEnum.Number:
        return text.trim() !== '' && !isNaN(Number(text));
      default:
        return true;
    }
  }
}
