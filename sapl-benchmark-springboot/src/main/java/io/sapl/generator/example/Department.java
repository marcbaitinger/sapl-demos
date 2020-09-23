package io.sapl.generator.example;

import io.sapl.generator.DomainActions;
import io.sapl.generator.DomainResource;
import io.sapl.generator.DomainRole;
import io.sapl.generator.DomainRole.ExtendedDomainRole;
import io.sapl.generator.DomainUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@RequiredArgsConstructor
public class Department {

    //TODO add resources only available internally (internalResources)

    private final String departmentName;
    private final int additionalDepartmentRoles;
    private final int additionalDepartmentResources;
    private final int numberOfSpecialActions;

    private final DomainActions domainActionsInternal;
    private final DomainActions domainActionsPublic;

    private List<DomainRole> departmentRoles = new ArrayList<>();
    private List<DomainResource> departmentResources = new ArrayList<>();

    //CRUD actions
    private List<String> departmentInternalActions = new ArrayList<>();
    private List<String> departmentPublicActions = new ArrayList<>();
    //additional actions (specialAction1,...)
    private List<String> departmentSpecialActions = new ArrayList<>();


    private List<DomainRole> rolesForPublicActions = new ArrayList<>();
    private List<DomainRole> rolesForSpecialActions = new ArrayList<>();

    private List<ExtendedDomainRole> extendedRolesForPublicActions = new ArrayList<>();
    private List<ExtendedDomainRole> extendedRolesForSpecialActions = new ArrayList<>();


    public void init() {
        createDepartmentRoles();
        createDepartmentResources();

        createInternalActions();
        createPublicActions();
        createSpecialActions();

        LOGGER.trace("initialized department: {}", departmentName);
    }

    private void createDepartmentResources() {
        departmentResources.add(new DomainResource(String.format("resource.%s.0",
                departmentName)));

        for (int i = 1; i <= additionalDepartmentResources; i++) {
            departmentResources.add(new DomainResource(String.format("resource.%s.%d",
                    departmentName, i)));
        }
    }

    private void createDepartmentRoles() {
        String mainDepartmentRoleName = ExampleProvider.DEPARTMENT_ROLE_MAP.getOrDefault(departmentName,
                String.format("role.%03d", DomainUtil.getNextRoleCount()));

        departmentRoles.add(new DomainRole(mainDepartmentRoleName));

        for (int i = 0; i < additionalDepartmentRoles; i++) {
            String roleAppendix = DomainUtil.getIOrDefault(ExampleProvider.EXAMPLE_GENERAL_ROLE_APPENDIX_LIST, i,
                    String.format("%03d", DomainUtil.getNextAppendixCount()));

            departmentRoles.add(new DomainRole(mainDepartmentRoleName + "-" + roleAppendix));
        }
    }


    private void createSpecialActions() {
        for (int i = 0; i < numberOfSpecialActions; i++) {
            departmentSpecialActions.add("specialAction" + i);
        }
    }

    private void createPublicActions() {
        for (DomainResource departmentResource : departmentResources) {
            departmentPublicActions
                    .addAll(domainActionsPublic.generateActionsForResource(departmentResource.getResourceName()));
        }
    }

    private void createInternalActions() {
        for (DomainResource departmentResource : departmentResources) {
            departmentInternalActions
                    .addAll(domainActionsInternal.generateActionsForResource(departmentResource.getResourceName()));
        }
    }

    public void addRolesForPublicActions(List<DomainRole> roles) {
        rolesForPublicActions.addAll(roles);
    }

    public void addExtendedRolesForPublicActions(List<ExtendedDomainRole> rolesForAction) {
        extendedRolesForPublicActions.addAll(rolesForAction);
    }

    public void addRolesForSpecialActions(List<DomainRole> roles) {
        rolesForSpecialActions.addAll(roles);
    }

    public void addExtendedRolesForSpecialActions(List<ExtendedDomainRole> rolesForAction) {
        extendedRolesForSpecialActions.addAll(rolesForAction);
    }

    public boolean isInternalAccessUnrestricted() {
        return domainActionsInternal.isUnrestrictedAccess();
    }

    public boolean isPublicAccessUnrestricted() {
        return domainActionsPublic.isUnrestrictedAccess();
    }

    public String departmentDetails() {
        return "Department{" +
                "departmentName='" + departmentName + '\'' +
                ", departmentRoles=" + departmentRoles +
                ", departmentResources=" + departmentResources +
                '}';
    }


}
