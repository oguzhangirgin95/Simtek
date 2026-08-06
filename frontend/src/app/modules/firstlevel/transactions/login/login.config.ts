import { FlowConfig, ValidatorEnum } from "../../../../../lib/base/baseconfig/config";

export const LoginConfig: FlowConfig = {
  config: {
    steps: [
      {
        step: 'start',
        showContinueButton: true,
        showBackButton: true,
        showHeader: false,
        showFooter: false,
         validation: [
          {
            id: 'username',
            validatorType: ValidatorEnum.Required,
            validationMessage: 'VALIDATION_REQUIRED | Username is required'
          },
          {
            id: 'username',
            validatorType: ValidatorEnum.Custom,
            customValidation: Validation,
            validationMessage: 'VALIDATION_CUSTOM | Username cannot be "admin"'
          },
          {
            id: 'username',
            validatorType: ValidatorEnum.Custom,
            customValidation: ValidationLength,
            validationMessage: 'VALIDATION_LENGTH_CUSTOM | Username cannot be higher than 6 characters'
          },
          {
            id: 'password',
            validatorType: ValidatorEnum.Required,
            validationMessage: 'VALIDATION_REQUIRED | Password is required'
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
