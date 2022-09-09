import { Directive, ElementRef, HostListener } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[spendingNumberOnly]'
})
export class NumberOnlyDirective {

  constructor(private el: ElementRef, private ngControl: NgControl) { }

  @HostListener('input', ['$event']) onInputChange(event) {
    const initalValue = this.el.nativeElement.value;
    let pos = initalValue.indexOf('.') > -1 ? initalValue.indexOf('.') : initalValue.length;
    const result = initalValue.substr(0, pos).replace(/[^0-9]*/g, '');
    this.el.nativeElement.value = result;
    this.ngControl.control.patchValue(result);
    if (initalValue !== this.el.nativeElement.value) {
      event.stopPropagation();
    }
  }
}
