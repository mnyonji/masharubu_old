import { Moment } from 'moment';

export const enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

export const enum Status {
  NEW = 'NEW',
  ACTIVE = 'ACTIVE',
  SUSPENDED = 'SUSPENDED',
  DEACTIVATED = 'DEACTIVATED'
}

export interface IDriver {
  id?: number;
  name?: string;
  phoneNbr?: string;
  emailAddr?: string;
  gender?: Gender;
  status?: Status;
  dateCreated?: Moment;
  dateValidated?: Moment;
  userLogin?: string;
  userId?: number;
  createdByLogin?: string;
  createdById?: number;
  validatedByLogin?: string;
  validatedById?: number;
  tourOperatorName?: string;
  tourOperatorId?: number;
}

export class Driver implements IDriver {
  constructor(
    public id?: number,
    public name?: string,
    public phoneNbr?: string,
    public emailAddr?: string,
    public gender?: Gender,
    public status?: Status,
    public dateCreated?: Moment,
    public dateValidated?: Moment,
    public userLogin?: string,
    public userId?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public validatedByLogin?: string,
    public validatedById?: number,
    public tourOperatorName?: string,
    public tourOperatorId?: number
  ) {}
}
