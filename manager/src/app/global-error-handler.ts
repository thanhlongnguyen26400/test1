import { ErrorHandler, Injectable } from '@angular/core';
@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor() {
  }
  handleError(error: any): void {
    const chunkFailedMessage = /Loading chunk [\d]+ failed/;
    console.log(error);
    if (chunkFailedMessage.test(error.message)) {
      window.location.reload();
    }
  }
}
