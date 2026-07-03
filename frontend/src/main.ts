import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { provideHttpClient,withInterceptors } from '@angular/common/http';
import { authInterceptor } from './app/core/auth/auth.interceptor';
import { provideZonelessChangeDetection } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';

registerLocaleData(localePt);
bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes), provideHttpClient(withInterceptors([authInterceptor])), provideZonelessChangeDetection()]
}).catch(error => console.error(error));
