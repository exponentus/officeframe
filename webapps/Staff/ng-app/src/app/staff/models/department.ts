import { Organization } from './organization';
import { Employee } from './employee';

export class Department {
    id: string;
    name: string;
    organization: Organization;
    leadDepartment: Department;
    boss: Employee;
    rank: number;
    type: any;
    locName: any;

    static convertToDto(m: Department) {
        return {
            id: m.id,
            name: m.name,
            organization: m.organization ? { id: m.organization.id } : null,
            leadDepartment: m.leadDepartment ? { id: m.leadDepartment.id } : null,
            boss: m.boss ? { id: m.boss.id } : null,
            rank: m.rank,
            type: m.type ? { id: m.type.id } : null,
            locName: m.locName
        };
    }
}
