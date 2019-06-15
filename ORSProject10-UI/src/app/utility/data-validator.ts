import { Injectable } from '@angular/core';

@Injectable()
export class DataValidator {

  constructor() {
  }

  trim(valueToWhite: string) {
    return valueToWhite.replace(/\s/g, "");
  }

  /**
   * Check is string is not empty
   * @param val 
   */
  isNotNull(val: string) {
    let flag = true;
    try {
      if (val) {
        val = this.trim(val);
        flag = val.length > 0;
      } else {
        flag = false;
      }
    } catch (error) {
      flag = false;
    }
    return flag;
  }


  /**
 * Is string is empty
 * @param val
 */
  isNull(val: any) {
    return !this.isNotNullObject(val);
  }

  /**
   * Is not null object
   */
  isNotNullObject(val: any) {
    let flag = true;
    try {
      if (val) {
        flag = true;
      } else {
        flag = false;
      }
    } catch (error) {
      flag = false;
    }
    return flag;
  }

  /**
   * Is null object
   */
  isNullObject(val: any) {
    return !this.isNotNullObject(val);
  }


  isTrue(val: boolean) {
    let flag = true;
    if (val) {
      flag = true;
    } else {
      flag = false;
    }
    return flag;
  }

  /**
   * 
   * @param val Convets string into number
   */
  toInt(val: any) {
    let returnVal = 0;
    if (val) {
      try {
        returnVal = parseInt(val);
        if (isNaN(returnVal)) {
          returnVal = 0;
        }
      } catch (error) {
        returnVal = 0;
      }
    }
    return returnVal;
  }

  toFloat(val: any) {
    let returnVal = 0;
    if (val) {
      try {
        returnVal = parseFloat(val);
        if (isNaN(returnVal)) {
          returnVal = 0;
        }
      } catch (error) {
        returnVal = 0;
      }
    }
    return returnVal;
  }

  /**
   * Create clone pobject
   * 
   * @param obj 
   */
  getClone(obj) {
    return JSON.parse(JSON.stringify(obj));
  }

}