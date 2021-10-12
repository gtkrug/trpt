package edu.gatech.gtri.trustmark.trpt.service.role;

import grails.gorm.transactions.Transactional;
import org.gtri.fj.data.List;

import static edu.gatech.gtri.trustmark.trpt.service.permission.PermissionUtility.roleListAdministrator;

@Transactional
public class RoleService {

    public List<RoleResponse> findAll(
            final String requesterUsername,
            final RoleFindAllRequest roleFindAllRequest) {

        return roleListAdministrator(requesterUsername)
                .map(RoleUtility::roleResponse);
    }
}
