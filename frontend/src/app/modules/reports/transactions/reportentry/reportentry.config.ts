import { FlowConfig } from "../../../../../lib/base/baseconfig/config";
import { ReportEntryControllerService } from "../../../../../lib/services/api/reportEntryController.service";

export const ReportEntryConfig: FlowConfig = {
  config: {
    steps: [
      {
        step: 'start',
        showContinueButton: true,
        showBackButton: true,
        validation: [],
      },
      {
        step: 'confirm',
        showContinueButton: true,
        showBackButton: true,
        validation: [],
        service:{
          serviceName: ReportEntryControllerService,
          methodName: "confirm",
          params:["confirmRequest"]
        }
      },
      {
        step: 'execute',
        showContinueButton: false,
        showBackButton: false,
        validation: [],
        service:{
          serviceName: ReportEntryControllerService,
          methodName: "execute",
          params:["executeRequest"]
        },
        keepState: true,
        buttons: [
          {
            id:"",
            label: '',
            navigate: '',
            color: 'primary',
            isVisible: "state.executeRequest.isVisible",
          },
        ],
      },
    ],
  }
};