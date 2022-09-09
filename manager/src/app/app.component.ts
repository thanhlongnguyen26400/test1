import { Component } from '@angular/core';
import { SubjectService } from './services/subject.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent{
  fullLoading:boolean = false;
  constructor(private subjectService:SubjectService){}
  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.subjectService.fullLoading.subscribe((res:boolean) => {
      this.fullLoading = res;
    })
  }
}
