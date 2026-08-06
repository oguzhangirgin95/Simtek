import { FlowConfig } from "../../../../../lib/base/baseconfig/config";

export const TasklistConfig: FlowConfig = {
  config: {
    steps: [
      {
        step: 'start',
        showContinueButton: false,
        showBackButton: false,
        validation: [],
      }
    ],
  }
};
