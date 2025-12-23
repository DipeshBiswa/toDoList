import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../authencation-service';


type LoginFormGroup = FormGroup<{username: FormControl<string>; password: FormControl<string>}>
@Component({
  selector: 'app-authencation-dashboard',
  imports: [],
  templateUrl: './authencation-dashboard.html',
  styleUrl: './authencation-dashboard.css',
})
export class AuthencationDashboard implements OnDestroy{
  loginForm: LoginFormGroup;
  submitted = false;
  mode: 'login' | 'register' = 'login';
  private readonly subscriptions = new Subscription();

  submit(){
    this.submitted = true;

    if(this.loginForm.invalid){
      this.loginForm.markAllAsTouched();
      return;
    }
    const {username, password} = this.loginForm.getRawValue();
    if(this.mode ==='login'){
      this.auth.login(username, password);
    }else{
      this.auth.register(username, password)
    }
  }
  constructor(
    private fb:FormBuilder,
    private auth: AuthenticationService,
  ){
    this.loginForm = this.fb.nonNullable.group({username: ['', Validators.required], password:['', Validators.required]});

    this.subscriptions.add(
      this.auth.loginFailed$.subscribe(()=>{
        if(this.mode !== 'login'){
          return;
        }
        this.loginForm.reset({username:'' ,password:''})
        this.submitted = false;
      })
    )
  }
  switchMode(): void{
    this.mode = this.mode === 'login' ? 'register': 'login';
    this.loginForm.reset({username: '', password:''});
    this.submitted = false;
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
