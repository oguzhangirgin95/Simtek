import { FlowConfig, ValidatorEnum } from "../../../../../lib/base/baseconfig/config";
import { TaskControllerService } from "../../../../../lib/services/api/taskController.service";

export const TaskassignConfig: FlowConfig = {
  config: {
    steps: [
      {
        step: 'start',
        showContinueButton: true,
        showBackButton: false,
        validation: [
          {
            id: 'policeId',
            value: 'State.Request.policeId',
            validatorType: ValidatorEnum.Required,
            validationMessage: 'VALIDATION_REQUIRED | Personel seçilmeli'
          },
          {
            id: 'type',
            value: 'State.Request.type',
            validatorType: ValidatorEnum.Required,
            validationMessage: 'VALIDATION_REQUIRED | Görev tipi seçilmeli'
          },
          {
            id: 'location',
            value: 'State.Request.location',
            validatorType: ValidatorEnum.Required,
            validationMessage: 'VALIDATION_REQUIRED | Görev yeri girilmeli'
          }
        ],
      },
      {
        step: 'confirm',
        showContinueButton: true,
        showBackButton: true,
        validation: [],
        service: {
          serviceName: TaskControllerService,
          methodName: 'taskAssignConfirm',
          params: ['Request']
        }
      },
      {
        step: 'execute',
        showContinueButton: false,
        showBackButton: false,
        validation: [],
        keepState: true,
        service: {
          serviceName: TaskControllerService,
          methodName: 'taskAssignExecute',
          params: ['Request']
        }
      },
    ],
  }
};
