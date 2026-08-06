export * from './loginController.service';
import { LoginControllerService } from './loginController.service';
export * from './reportEntryController.service';
import { ReportEntryControllerService } from './reportEntryController.service';
export * from './resourceController.service';
import { ResourceControllerService } from './resourceController.service';
export const APIS = [LoginControllerService, ReportEntryControllerService, ResourceControllerService];
