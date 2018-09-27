import { Injectable } from '@angular/core';

import {
    IAction, IEntity, IDto, IEntityService, IFormSchema,
    AppService, DataService, createApiUrl
} from '@nb/core';

import { REFERENCE_URL } from './reference.constants';
import { convertToDto } from './converter-factory';

@Injectable()
export class ReferenceService implements IEntityService<IEntity> {

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

    getFormSchema(kind: string): IFormSchema[] {
        let formSchemas = getFormSchemas(this.appService.languages);
        return formSchemas[kind] || formSchemas._default;
    }
}

const CHANGE_NAMED_URL_ACTION: IAction = {
    customID: 'CHANGE_NAMED_URL',
    caption: 'edit',
    confirm: {
        type: 'CONFIRM',
        title: 'confirm_action',
        message: 'url_syntax_warning'
    }
};

function getFormSchemas(languages: any) {
    return {
        _default: [{
            tabTitle: 'properties',
            fieldsets: [{
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Country: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'code',
                    name: 'code',
                    values: { payloadEnum: 'countryCodes' },
                    className: 'span2',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Region: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'type',
                    name: 'type',
                    className: 'span7',
                    values: {
                        url: REFERENCE_URL.API_REGION_TYPES
                    },
                    required: true
                }, {
                    type: 'select',
                    label: 'country',
                    name: 'country',
                    values: {
                        url: REFERENCE_URL.API_COUNTRIES
                    },
                    className: 'span7',
                    required: true
                }, {
                    type: 'boolean',
                    name: 'primary',
                    values: { label: 'is_primary' }
                }, {
                    type: 'text',
                    label: 'coordinates',
                    name: 'latLng',
                    className: 'span7'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: Object.assign({}, CHANGE_NAMED_URL_ACTION)
                }]
            }]
        }],
        District: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'region',
                    name: 'region',
                    className: 'span7',
                    values: {
                        url: REFERENCE_URL.API_REGIONS
                    },
                    required: true
                }, {
                    type: 'text',
                    label: 'coordinates',
                    name: 'latLng',
                    className: 'span7'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Locality: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'type',
                    name: 'type',
                    className: 'span7',
                    values: {
                        url: REFERENCE_URL.API_LOCALITY_TYPES
                    }
                }, {
                    type: 'select',
                    label: 'region',
                    name: 'region',
                    className: 'span7',
                    values: {
                        url: REFERENCE_URL.API_REGIONS
                    }
                }, {
                    type: 'select',
                    label: 'district',
                    name: 'district',
                    className: 'span7',
                    values: {
                        url: (model): string => {
                            let _url = REFERENCE_URL.API_DISTRICTS;
                            return model && model.region ? `${_url}?region=${model.region.id}` : _url;
                        },
                        preload: true
                    }
                }, {
                    type: 'boolean',
                    name: 'districtCenter',
                    values: { label: 'is_district_center' }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        CityDistrict: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'locality',
                    name: 'locality',
                    className: 'span7',
                    values: {
                        url: REFERENCE_URL.API_LOCALITIES
                    }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Street: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'locality',
                    name: 'locality',
                    className: 'span7',
                    values: {
                        url: REFERENCE_URL.API_LOCALITIES
                    }
                }, {
                    type: 'select',
                    label: 'city_district',
                    name: 'cityDistrict',
                    className: 'span7',
                    values: {
                        url: REFERENCE_URL.API_CITY_DISTRICTS
                    }
                }, {
                    type: 'number',
                    label: 'identifier',
                    name: 'streetId',
                    className: 'span3'
                }, {
                    type: 'textarea',
                    label: 'alt_name',
                    name: 'altName',
                    className: 'span7'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Position: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'number',
                    label: 'rank',
                    name: 'rank',
                    className: 'span2'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        DocumentLanguage: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'code',
                    name: 'code',
                    values: { payloadEnum: 'languageCodes' },
                    className: 'span2',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        RegionType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'region_code',
                    name: 'code',
                    values: { payloadEnum: 'regionCodes' },
                    className: 'span3'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        LocalityType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'locality_code',
                    name: 'code',
                    values: { payloadEnum: 'localityCodes' },
                    className: 'span3'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        DocumentType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'text',
                    label: 'prefix',
                    name: 'prefix',
                    className: 'span2'
                }, {
                    type: 'text',
                    label: 'category',
                    name: 'category',
                    className: 'span7',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        DocumentSubject: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'text',
                    label: 'category',
                    name: 'category',
                    className: 'span7',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        ControlType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'number',
                    label: 'default_hours',
                    name: 'defaultHours',
                    className: 'span1',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        TextTemplate: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'text',
                    label: 'category',
                    name: 'category',
                    className: 'span7',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Tag: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'category',
                    name: 'category',
                    values: { payloadEnum: 'tagCategories' },
                    className: 'span7'
                }, {
                    type: 'color',
                    label: 'color',
                    name: 'color',
                    className: 'span2'
                }, {
                    type: 'boolean',
                    name: 'hidden',
                    values: { label: 'is_hidden' }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        TaskType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'text',
                    label: 'prefix',
                    name: 'prefix',
                    className: 'span2',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        DemandType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'text',
                    label: 'prefix',
                    name: 'prefix',
                    className: 'span2',
                    required: true
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        BuildingState: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'boolean',
                    name: 'requireDate',
                    values: { label: 'require_date' }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        UnitType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'category',
                    name: 'category',
                    className: 'span4',
                    values: {
                        payloadEnum: 'unitCategories'
                    }
                }, {
                    type: 'number',
                    label: 'unit_factor',
                    name: 'factor',
                    className: 'span2'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span8',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        IndustryType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'category',
                    name: 'category',
                    className: 'span8',
                    values: {
                        url: REFERENCE_URL.API_ACTIVITY_TYPE_CATEGORIES
                    }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span8',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Revenue: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'category',
                    name: 'category',
                    className: 'span8',
                    values: {
                        url: REFERENCE_URL.API_REVENUE_CATEGORY
                    }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span8',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span8',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        Expenditure: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'category',
                    name: 'category',
                    className: 'span8',
                    values: {
                        url: REFERENCE_URL.API_EXPENDITURE_CATEGORY
                    }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span8',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span8',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        AsOf: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'date',
                    label: 'as_of_by_date',
                    name: 'asOfByDate',
                    className: 'span2'
                }, {
                    type: 'boolean',
                    name: 'allowedToPublish',
                    values: { label: 'allowed_to_publish' }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span8',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span8',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        BuildingMaterial: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'textarea',
                    label: 'alt_name',
                    name: 'altName',
                    className: 'span7'
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span7',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span7',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }],
        StatisticType: [{
            tabTitle: 'properties',
            fieldsets: [/*{
                fields: [{
                    type: 'select',
                    label: 'parent',
                    name: 'parent',
                    className: 'span8',
                    values: {
                        url: '/Reference/api/statistic-types'
                    }
                },*/ {
                    title: 'localized_names',
                    fields: [{
                        type: 'localizedName',
                        name: 'locName',
                        hideLabel: true,
                        className: 'span8',
                        values: { enum: languages }
                    }]
                }, {
                    title: 'URL',
                    fields: [{
                        type: 'text',
                        label: 'name',
                        name: 'name',
                        className: 'span8',
                        disabled: true,
                        onClick: CHANGE_NAMED_URL_ACTION
                    }]
                }]
        }],
        StatisticIndicatorType: [{
            tabTitle: 'properties',
            fieldsets: [{
                fields: [{
                    type: 'select',
                    label: 'statistic_type',
                    name: 'statisticType',
                    className: 'span8',
                    values: {
                        url: '/Reference/api/statistic-types'
                    }
                }, {
                    type: 'select',
                    label: 'unit_type',
                    name: 'unitType',
                    className: 'span4',
                    values: {
                        url: '/Reference/api/unit-types',
                        // groupBy: 'category',
                        // groupSelectable: false
                    }
                }]
            }, {
                title: 'localized_names',
                fields: [{
                    type: 'localizedName',
                    name: 'locName',
                    hideLabel: true,
                    className: 'span8',
                    values: { enum: languages }
                }]
            }, {
                title: 'URL',
                fields: [{
                    type: 'text',
                    label: 'name',
                    name: 'name',
                    className: 'span8',
                    disabled: true,
                    onClick: CHANGE_NAMED_URL_ACTION
                }]
            }]
        }]
    };
}
