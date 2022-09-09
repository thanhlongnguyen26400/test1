import { Pipe, PipeTransform } from '@angular/core';
import { environment } from 'environments/environment';

@Pipe({
  name: 'imageConvert'
})
export class ImageConvertPipe implements PipeTransform {

  transform(url: any) {
    if (validURL(url)) {
      return url;
    } else if (isBase64Image(url)) {
      return url;
    } else if (url == null) {
      return "assets/img/default-avatar.png";
    } else {
      return `${environment.apiUrl}` + url;
    }
  }
}

function validURL(str) {
  var RegExp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
  if(RegExp.test(str)){
      return true;
  }else{
      return false;
  }
}

function isBase64Image(str) {
  if (str ==='' || str === undefined || str === null){ return false; }

  if (str.indexOf('data:image/') == 0) {
    return true;
  } else {
    return false;
  }

}
