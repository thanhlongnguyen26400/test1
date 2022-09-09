import { Component } from '@angular/core';
import { HelperService } from 'app/services/helper.service';
import { ToastrService } from "ngx-toastr";


@Component({
    selector: 'notifications-cmp',
    moduleId: module.id,
    templateUrl: 'notifications.component.html'
})

export class NotificationsComponent{
  constructor(private toastr: ToastrService, private helperService:HelperService) {}
  showNotification(from, align) {
    const color = Math.floor(Math.random() * 5 + 1);
    this.helperService.showError('','test show toast');
    this.helperService.showSuccess('','test show toast');
  }
}
