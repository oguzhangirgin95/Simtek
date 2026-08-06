import { FlowConfig } from "../../../../../lib/base/baseconfig/config";

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
          serviceName: "",
          methodName: "confirm",
          params:[]
        }
      },
      {
        step: 'execute',
        showContinueButton: false,
        showBackButton: false,
        validation: [],
        service:{
          serviceName: "",
          methodName: "execute",
          params:[]
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