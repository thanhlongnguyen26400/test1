import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environments/environment';
import { param } from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient:HttpClient) { }

  login(params){
    const url = `${environment.apiUrl}login`;
    return this.httpClient.post(url,params);
  }
  register(params){
    const url = `${environment.apiUrl}register`;
    return this.httpClient.post(url,params);
  }
  getSummary(){
    const url = `${environment.apiUrl}dashboard/summary`;
    return this.httpClient.get(url);
  }
  getHistory(params){
    const url = `${environment.apiUrl}history`;
    return this.httpClient.get(url,{params:params});
  }
  getHistoryType(){
    const url = `${environment.apiUrl}history-type/all`;
    return this.httpClient.get(url);
  }
  createHistory(params){
    const url = `${environment.apiUrl}history/add`;
    return this.httpClient.post(url,params);
  }
  updateHistory(params){
    const url = `${environment.apiUrl}history/update`;
    return this.httpClient.post(url,params);
  }
  createHistoryType(params){
    const url = `${environment.apiUrl}history-type/add`;
    return this.httpClient.post(url,params);
  }
  updateHistoryType(params){
    const url = `${environment.apiUrl}history-type/update`;
    return this.httpClient.post(url,params);
  }
  deleteHistoryType(id){
    const url = `${environment.apiUrl}history-type/delete/${id}`;
    return this.httpClient.get(url);
  }
  getHistoryDetail(id){
    const url = `${environment.apiUrl}history/${id}`;
    return this.httpClient.get(url);
  }
  deleteHistory(id){
    const url = `${environment.apiUrl}history/delete/${id}`;
    return this.httpClient.get(url);
  }
  getProfile(){
    const url = `${environment.apiUrl}user/profile`;
    return this.httpClient.get(url);
  }
  updateProfile(params){
    const url = `${environment.apiUrl}user/profile/update`;
    return this.httpClient.post(url,params);
  }
  getUserProfile(id){
    const url = `${environment.apiUrl}user/profile/${id}`;
    return this.httpClient.get(url);
  }
  getNotification(params){
    const url = `${environment.apiUrl}notification`;
    return this.httpClient.get(url,{params: params});
  }
  signNotification(){
    const url = `${environment.apiUrl}notification/read`;
    return this.httpClient.get(url);
  }
  search(params){
    const url = `${environment.apiUrl}user/search`;
    return this.httpClient.get(url,{params:params});
  }
  uploadImage(params){
    const url = `${environment.apiUrl}uploadFile`;
    return this.httpClient.post(url,params);
  }
}

