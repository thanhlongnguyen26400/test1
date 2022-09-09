import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from 'app/pages/auth-module/components/login/login.component';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthLayoutComponent } from 'app/layouts/auth-layout/auth-layout.component';
import { RegisterComponent } from './components/register/register.component';
import { DirectivesModule } from 'app/directives/directives.module';



@NgModule({
  declarations: [AuthLayoutComponent, LoginComponent, RegisterComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '', component: AuthLayoutComponent,
        children: [
          { path: '', redirectTo: 'login' },
          { path: 'login', component: LoginComponent },
          { path: 'register', component: RegisterComponent }
        ]
      }
    ]),
    FormsModule,
    ReactiveFormsModule,
    DirectivesModule
  ]
})
export class AuthModuleModule { }
