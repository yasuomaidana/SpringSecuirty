import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LocalStorageService } from 'ngx-localstorage';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { loginRequestPayload } from 'src/app/login/login/payload/login-request.payload';
import { environment } from 'src/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient:HttpClient,private localStorage:LocalStorageService) { }

  login(loginRequestPayload:loginRequestPayload):Observable<any>{
    return this.httpClient.get<any>(environment.backend_host+"/login2",
    {responseType:'text' as 'json'});
  }
}
