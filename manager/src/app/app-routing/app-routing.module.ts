import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const AppRoutes: Routes = [
  {
    path: '', loadChildren: () => import('../pages/admin-module/admin.module').then(m => m.AdminModule)
  },
  {
    path: 'auth', loadChildren: () => import('../pages/auth-module/auth-module.module').then(m => m.AuthModuleModule)
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(AppRoutes, { preloadingStrategy: PreloadAllModules, scrollPositionRestoration: 'enabled' })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
