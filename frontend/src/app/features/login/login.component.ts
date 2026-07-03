import { CommonModule } from '@angular/common';
import { Component,signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute,Router } from '@angular/router';
import { finalize,timeout } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
@Component({selector:'app-login',standalone:true,imports:[CommonModule,FormsModule],templateUrl:'./login.component.html',styleUrl:'./login.component.css'})
export class LoginComponent{email='';password='';readonly loading=signal(false);readonly error=signal('');constructor(private readonly auth:AuthService,private readonly router:Router,route:ActivatedRoute){if(route.snapshot.queryParamMap.get('sessionExpired'))this.error.set('Sessão expirada. Faça login novamente.');}enter():void{this.loading.set(true);this.error.set('');this.auth.login(this.email,this.password).pipe(timeout(8000),finalize(()=>this.loading.set(false))).subscribe({next:()=>void this.router.navigate(['/dashboard']),error:r=>{this.error.set(r.error?.message||'E-mail ou senha inválidos.');}});}}
