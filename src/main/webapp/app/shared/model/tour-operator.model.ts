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
  validatedBy?: number;
  dateValidated?: Moment;
  physicalAddress?: string;
}

export class TourOperator implements ITourOperator {
  constructor(
    public id?: number,
    public name?: string,
    public phoneNbr?: string,
    public emilAddr?: string,
    public status?: Status,
    public dateCreated?: Moment,
    public validatedBy?: number,
    public dateValidated?: Moment,
    public physicalAddress?: string
  ) {}
}
