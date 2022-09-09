import { Directive, ElementRef, HostListener, Input, Optional, Renderer2 } from '@angular/core';
import { NgControl, NgModel } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Directive({
  selector: '[spendingInputMaxLength]'
})
export class InputMaxLengthDirective {

  @Input() spendingInputMaxLength: number;
  private div: HTMLDivElement;
  private destroyed$ = new Subject();

  constructor(
    private el: ElementRef,
    private renderer: Renderer2,
    @Optional() private ngModel: NgModel,
    @Optional() private control: NgControl
  ) { }

  @HostListener('input', ['$event']) onChange(event) {
    if (!this.ngModel) {
      this.update(event.target.value.length);
    }
  }

  ngOnInit() {    
    this.renderer.setAttribute(this.el.nativeElement, 'maxLength', this.spendingInputMaxLength.toString());
    if (this.ngModel) {
      this.ngModel.valueChanges.pipe(takeUntil(this.destroyed$)).subscribe(value => {
        this.update(value.length);
      })
    }
    if(this.control){
      this.control.valueChanges.pipe(takeUntil(this.destroyed$)).subscribe(value =>{
        this.update(value.length);
      });
    }
  }

  ngAfterViewInit() {
    this.div = this.renderer.createElement('p');
    this.div.classList.add('count-text');
    this.renderer.insertBefore(this.el.nativeElement.parentNode, this.div, this.el.nativeElement.nextSibling);

    setTimeout(() => {
      this.update(this.el.nativeElement.value.length); 
    }, 1000);
  }

  ngOnDestroy() {
    this.destroyed$.next();
    this.destroyed$.complete();
    if (this.div) {
      this.div.remove();
    }
  }

  private update(length: number) { //`${this.spendingInputMaxLength - length} characters left`
    this.renderer.setProperty(this.div, 'innerText', `${this.spendingInputMaxLength - length} character left`);
  }

}
