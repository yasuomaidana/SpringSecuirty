import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-localstorage';
import { Observable } from 'rxjs';
import { loginRequestPayload } from 'src/app/login/login/payload/login-request.payload';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators'
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient:HttpClient,private localStorage:LocalStorageService) { }

  login(loginRequestPayload:loginRequestPayload):Observable<boolean>{
    return this.httpClient.get<any>(environment.backend_host+"/login")
    .pipe(map(answer=>{
      console.log(answer);
      return true;
    }));
  }
}
