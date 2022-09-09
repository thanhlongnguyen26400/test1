import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { SubjectService } from './subject.service';

@Injectable({
  providedIn: 'root'
})
export class HelperService {

  constructor(
    private toastr: ToastrService,
    private subjectService: SubjectService) { }
  showSuccess(title, content, options = null) {
    const template = this.getTemplate(content);
    this.toastr.success(template, title,
      {
        toastClass: "alert alert-success alert-with-icon",
      });
  }
  showError(title, content, options = null) {
    const template = this.getTemplate(content);
    this.toastr.error(template, title, {
      toastClass: "alert alert-danger alert-with-icon",
    });
  }
  showInfo(title, content) {
    const tempate = this.getTemplate(content);
    this.toastr.show(tempate, title, { toastClass: "alert alert-info alert-with-icon", });
  }
  showWarning(title, content) {
    const tempate = this.getTemplate(content);
    this.toastr.show(tempate, title, { toastClass: "alert alert-warning alert-with-icon", });
  }
  showToast(title, content) {
    const tempate = this.getTemplate(content);
    this.toastr.show(tempate, title, { toastClass: "alert alert-primary alert-with-icon", });
  }
  private getTemplate(content) {
    return `<span data-notify="icon" class="nc-icon nc-bell-55"></span>
    <span data-notify="message">${content}</span>`;
  }
  showFullLoading() {
    this.subjectService.fullLoading.next(true);
  }
  hideFullLoading() {
    this.subjectService.fullLoading.next(false);
  }
  markFormGroupTouched(formGroup) {
    (Object as any).values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control.controls) {
        this.markFormGroupTouched(control);
      }
    });
  }
  validateDate(date, time, isUTC) {
    const validate = moment(date + " " + time);
    if (!validate.isValid()) return [null, null];
    if (isUTC) { //convert to local
      const obj = moment.utc(date + " " + time);
      return [obj.local().format("yyyy-MM-DD"), obj.local().format("HH:mm")];
    }
    const obj = moment(date + " " + time);
    return [obj.utc().format("yyyy-MM-DD"), obj.utc().format("HH:mm")];
  }
  slugify(str) {
    str = str.replace(/^\s+|\s+$/g, '');

    // Make the string lowercase
    str = str.toLowerCase();

    // Remove accents, swap ñ for n, etc
    var from = "ÁÄÂÀÃÅČÇĆĎÉĚËÈÊẼĔȆÍÌÎÏŇÑÓÖÒÔÕØŘŔŠŤÚŮÜÙÛÝŸŽáäâàãåčçćďéěëèêẽĕȇíìîïňñóöòôõøðřŕšťúůüùûýÿžþÞĐđßÆa·/_,:;";
    var to = "AAAAAACCCDEEEEEEEEIIIINNOOOOOORRSTUUUUUYYZaaaaaacccdeeeeeeeeiiiinnooooooorrstuuuuuyyzbBDdBAa------";
    for (var i = 0, l = from.length; i < l; i++) {
      str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
    }

    // Remove invalid chars
    str = str.replace(/[^a-z0-9 -]/g, '')
      // Collapse whitespace and replace by -
      .replace(/\s+/g, '-')
      // Collapse dashes
      .replace(/-+/g, '-');

    return str;
  }
  addAlpha(color: string, opacity: number): string {
    // coerce values so ti is between 0 and 1.
    const _opacity = Math.round(Math.min(Math.max(opacity || 1, 0), 1) * 255);
    return color + _opacity.toString(16).toUpperCase();
}
}
