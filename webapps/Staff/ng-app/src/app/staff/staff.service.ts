import { Injectable } from '@angular/core';

import {
    IEntity, IDto, IColumnOptions, IFormSchema, IEntityService,
    AppService, DataService,
    createApiUrl,
    Attachment
} from '@nb/core';

import { REFERENCE_URL } from '@nb/core';
import { STAFF_URL } from './staff.constants';
import { convertToDto } from './converter-factory';
import { Employee } from './models';

@Injectable()
export class StaffService implements IEntityService<IEntity> {

    constructor(
        private appService: AppService,
        private dataService: DataService
    ) { }

    fetchUrl(url: string, params: any) {
        return this.dataService.apiGet(createApiUrl(url), params);
    }

    convertToDto(model: IEntity): IDto {
        return convertToDto(model);
    }

    // getFormSchema(kind: string): IFormSchema[] {
    //     let formSchemas = getFormSchemas(this.appService.languages);
    //     return formSchemas[kind] || formSchemas._default;
    // }
}

function getFormSchemas(languages: any) {
    return {
        _default: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span8',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    hideLabel: true,
                    className: 'span8',
                    values: { enum: languages }
                }]
            }]
        }],
        // Organization: [{
        //     tabTitle: 'properties',
        //     fieldsets: [{
        //         fields: [{
        //             type: 'text',
        //             label: 'name',
        //             name: 'name',
        //             className: 'span8',
        //             required: true
        //         }, {
        //             type: 'select',
        //             label: 'org_category',
        //             name: 'orgCategory',
        //             values: {
        //                 url: REFERENCE_URL.API_ORG_CATEGORIES
        //             },
        //             className: 'span4',
        //             required: true
        //         }, {
        //             type: 'text',
        //             label: 'biz_id',
        //             name: 'bizID',
        //             className: 'span4',
        //             required: true
        //         }, {
        //             type: 'select',
        //             label: 'organization_labels',
        //             name: 'labels',
        //             values: {
        //                 multiple: true,
        //                 url: STAFF_URL.API_ORGANIZATION_LABELS
        //             },
        //             className: 'span8'
        //         }]
        //     }, {
        //         title: 'localized_names',
        //         fields: [{
        //             type: 'localizedName',
        //             hideLabel: true,
        //             className: 'span8',
        //             values: { enum: languages }
        //         }]
        //     }]
        // }],
        // Department: [{
        //     tabTitle: 'properties',
        //     fieldsets: [{
        //         fields: [{
        //             type: 'select',
        //             label: 'organization',
        //             name: 'organization',
        //             values: {
        //                 url: STAFF_URL.API_ORGANIZATIONS
        //             },
        //             className: 'span6',
        //             required: true
        //         }, {
        //             type: 'select',
        //             label: 'lead_department',
        //             name: 'leadDepartment',
        //             values: {
        //                 url: STAFF_URL.API_DEPARTMENTS
        //             },
        //             className: 'span6'
        //         }, {
        //             type: 'text',
        //             label: 'name',
        //             name: 'name',
        //             className: 'span6',
        //             required: true
        //         }, {
        //             type: 'select',
        //             label: 'type',
        //             name: 'type',
        //             values: {
        //                 url: REFERENCE_URL.API_DEPARTMENT_TYPES
        //             },
        //             className: 'span6'
        //         }, {
        //             type: 'number',
        //             label: 'rank',
        //             name: 'rank',
        //             className: 'span2'
        //         }]
        //     }, {
        //         title: 'localized_names',
        //         fields: [{
        //             type: 'localizedName',
        //             hideLabel: true,
        //             className: 'span6',
        //             values: { enum: languages }
        //         }]
        //     }]
        // }],
        // Employee: [{
        //     tabTitle: 'properties',
        //     className: 'employee-form',
        //     fieldsets: [{
        //         className: 'fieldset-user-avatar',
        //         fields: [{
        //             type: 'attachment',
        //             name: 'avatar',
        //             accept: 'image/*',
        //             maxFileCount: 1,
        //             computeAttachmentUrl: (model: Employee, att: Attachment) => {
        //                 if (att.realFileName === 'no_avatar.png') {
        //                     return '/Staff/img/nophoto.png';
        //                 } else if (att.base64) {
        //                     return att.base64;
        //                 } else {
        //                     return `${model.avatarURL}?t=${Date.now()}`;
        //                 }
        //             }
        //         }]
        //     }, {
        //         className: 'fieldset-user-fields',
        //         fields: [{
        //             type: 'text',
        //             label: 'name_surname',
        //             name: 'name',
        //             className: 'span6',
        //             required: true
        //         }, {
        //             type: 'text',
        //             label: 'iin',
        //             name: 'iin',
        //             className: 'span3 span-sm-full'
        //         }, {
        //             type: 'select',
        //             label: 'organization',
        //             name: 'organization',
        //             values: {
        //                 url: STAFF_URL.API_ORGANIZATIONS
        //             },
        //             className: 'span6',
        //             required: true
        //         }, {
        //             type: 'select',
        //             label: 'department',
        //             name: 'department',
        //             values: {
        //                 url: STAFF_URL.API_DEPARTMENTS
        //             },
        //             className: 'span6'
        //         }, {
        //             type: 'number',
        //             label: 'rank',
        //             name: 'rank',
        //             className: 'span2'
        //         }, {
        //             type: 'select',
        //             label: 'position',
        //             name: 'position',
        //             values: {
        //                 url: REFERENCE_URL.API_POSITIONS
        //             },
        //             className: 'span6'
        //         }, {
        //             type: 'select',
        //             label: 'roles',
        //             name: 'roles',
        //             values: {
        //                 multiple: true,
        //                 url: STAFF_URL.API_ROLES
        //             },
        //             className: 'span6'
        //         }, {
        //             type: 'select',
        //             label: 'login_name',
        //             name: 'login',
        //             className: 'span6',
        //             required: true,
        //             values: {
        //                 payloadEnum: 'userLogins'
        //             }
        //         }]
        //     }]
        // }],
        // Role: [{
        //     tabTitle: 'properties',
        //     fieldsets: [{
        //         fields: [{
        //             type: 'select',
        //             label: 'name',
        //             name: 'name',
        //             className: 'span8',
        //             required: true,
        //             values: { payloadEnum: 'roles' },
        //         }]
        //     }, {
        //         title: 'localized_names',
        //         fields: [{
        //             type: 'localizedName',
        //             hideLabel: true,
        //             className: 'span8',
        //             values: { enum: languages }
        //         }]
        //     }]
        // }],
        // organizationLabel: [{
        //     tabTitle: 'properties',
        //     fieldsets: [{
        //         fields: [{
        //             type: 'text',
        //             label: 'name',
        //             name: 'name',
        //             className: 'span8',
        //             required: true
        //         }]
        //     }, {
        //         title: 'localized_names',
        //         fields: [{
        //             type: 'localizedName',
        //             hideLabel: true,
        //             className: 'span8',
        //             values: { enum: languages }
        //         }]
        //     }]
        // }],
        // Individual: [{
        //     tabTitle: 'properties',
        //     fieldsets: [{
        //         fields: [{
        //             type: 'text',
        //             label: 'name',
        //             name: 'name',
        //             className: 'span8',
        //             required: true
        //         }, {
        //             type: 'text',
        //             label: 'bin',
        //             name: 'bin',
        //             className: 'span4',
        //             required: true
        //         }, {
        //             type: 'select',
        //             label: 'individual_labels',
        //             name: 'labels',
        //             values: {
        //                 multiple: true,
        //                 url: STAFF_URL.API_INDIVIDUAL_LABELS
        //             },
        //             className: 'span8'
        //         }]
        //     }]
        // }]
    };
}
