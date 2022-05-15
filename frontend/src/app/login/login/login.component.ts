import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { loginRequestPayload } from './payload/login-request.payload';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  constructor(private authService:AuthService) { }

  ngOnInit(): void {
  }

  login(){
    console.log("Doing login");

    const loginRequest:loginRequestPayload = {
      username:"linda",password:"pass"
    }
    this.authService.login(loginRequest)
    .subscribe((ans:any)=>console.log(ans));
    this.authService.login2(loginRequest).subscribe(ans=>console.log(ans));
  }
}
