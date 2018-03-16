import { IEntity, IDto, IDtoConverter } from '@nb/core';

import { Organization, Department, Employee } from './models';

export function getConverter(entityKind: string): IDtoConverter {
    switch (entityKind) {
        case 'Organization':
            return Organization;
        case 'Department':
            return Department;
        case 'Employee':
            return Employee;
        default:
            return DefaultConverter;
    }
}

export class DefaultConverter {
    static convertToDto(model: IEntity): IDto {
        return model;
    }
}

export function convertToDto(model: IEntity): IDto {
    return getConverter(model.kind).convertToDto(model);
}
