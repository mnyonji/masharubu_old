import { Moment } from 'moment';

export const enum Status {
  NEW = 'NEW',
  ACTIVE = 'ACTIVE',
  SUSPENDED = 'SUSPENDED',
  DEACTIVATED = 'DEACTIVATED'
}

export interface ITourOperator {
  id?: number;
  name?: string;
  phoneNbr?: string;
  emilAddr?: string;
  status?: Status;
  dateCreated?: Moment;
  dateValidated?: Moment;
  physicalAddress?: string;
  userLogin?: string;
  userId?: number;
  createdByLogin?: string;
  createdById?: number;
  validatedByLogin?: string;
  validatedById?: number;
}

export class TourOperator implements ITourOperator {
  constructor(
    public id?: number,
    public name?: string,
    public phoneNbr?: string,
    public emilAddr?: string,
    public status?: Status,
    public dateCreated?: Moment,
    public dateValidated?: Moment,
    public physicalAddress?: string,
    public userLogin?: string,
    public userId?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public validatedByLogin?: string,
    public validatedById?: number
  ) {}
}
