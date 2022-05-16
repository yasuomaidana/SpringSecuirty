import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';
import { loginRequestPayload } from './payload/login-request.payload';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  loginForm = this.formBuilder.group({
    username:"",
    password:""
  })

  constructor(private authService:AuthService, private formBuilder:FormBuilder) { }

  ngOnInit(): void {
  }

  login(){

    const loginRequest:loginRequestPayload = {
      username:this.loginForm.value["username"],password:this.loginForm.value["password"]
    }
    this.authService.login(loginRequest)
    .subscribe((ans:any)=>console.log("logging",ans),error=>console.log("error",error));

    this.authService.testGetMethod().subscribe(ans=>console.log("Testing get method",ans));
  }
}
