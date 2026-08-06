export interface ServiceConfig {
  serviceName: string;
  methodName: string;
  params: any[];
}

export interface ValidationRule {
  required?: boolean;
  regex?: string;
}

export interface ValidationConfig {
  [key: string]: ValidationRule;
}

export enum ValidatorEnum {
  Required = 'required',
  Regex = 'regex',
  MinLength = 'minLength',
  MaxLength = 'maxLength',
  Email = 'email',
  Number = 'number',
  Custom = 'custom',
}

export interface ValidationRuleConfig {
  id: string;
  validatorType?: ValidatorEnum;
  customValidation?: (value: unknown, element?: HTMLInputElement) => boolean;
  validationMessage: string;
  regex?: string;
  minLength?: number;
  maxLength?: number;
}

export interface ValidationError {
  id: string;
  message: string;
}

export interface FlowStep {
  step: string;
  validation: ValidationRuleConfig[];
  showContinueButton?: boolean;
  showBackButton?: boolean;
  service?: ServiceConfig;
  disableLayout?: boolean;
  keepState?: boolean;
  buttons?: FlowButton[];
}

export type FlowButtonVariant = 'primary' | 'secondary' | 'outline';

export interface FlowButtonVisibilityContext {
  get<T>(key: string): T | undefined;
}

export interface FlowButton {
  id: string;
  label: string;
  navigate: string;
  color?: FlowButtonVariant;
  variant?: FlowButtonVariant;
  isVisible?: boolean | string | ((ctx: FlowButtonVisibilityContext) => boolean);
}

export interface FlowConfig {
  config: {
    steps: FlowStep[];
  };
}