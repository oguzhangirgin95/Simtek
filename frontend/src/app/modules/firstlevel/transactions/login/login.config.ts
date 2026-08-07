import { FlowConfig, ValidatorEnum } from "../../../../../lib/base/baseconfig/config";

export const LoginConfig: FlowConfig = {
  config: {
    steps: [
      {
        step: 'start',
        showContinueButton: false,
        showBackButton: false,
        showHeader: false,
        showFooter: false,
         validation: [
          {
            id: 'username',
            value: 'State.Request.username',
            validatorType: ValidatorEnum.Required,
            validationMessage: 'VALIDATION_REQUIRED | Kullanıc adını giriniz.'
          },
          {
            id: 'username',
            value: 'State.Request.username',
            validatorType: ValidatorEnum.Custom,
            customValidation: Validation,
            validationMessage: 'VALIDATION_CUSTOM | Kullanıcı adı "admin" olamaz'
          },
          {
            id: 'username',
            value: 'State.Request.username',
            validatorType: ValidatorEnum.Custom,
            customValidation: ValidationLength,
            validationMessage: 'VALIDATION_LENGTH_CUSTOM | Kullanıcı adı 6 karakterden fazla olamaz'
          },
          {
            id: 'password',
            value: 'State.Request.password',
            validatorType: ValidatorEnum.Required,
            validationMessage: 'VALIDATION_REQUIRED | Parolayı giriniz.'
          }
        ],
      }
    ],
  }
};


export function Validation(value: any) {
  if (value?.trim().toLowerCase() === 'admin') {
    return false;
  }

  return true;
}

export function ValidationLength(value: any) {
  if (value?.length>5) {
    return false;
  }

  return true;
}
