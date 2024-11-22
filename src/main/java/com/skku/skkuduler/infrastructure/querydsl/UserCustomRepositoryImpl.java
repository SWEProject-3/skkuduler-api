package com.skku.skkuduler.infrastructure.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.skkuduler.domain.department.QDepartment;
import com.skku.skkuduler.domain.user.QPermission;
import com.skku.skkuduler.domain.user.QUser;
import com.skku.skkuduler.dto.response.DepartmentPermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public DepartmentPermissionDto getDepartmentPermissionDto(Long userId) {
        QUser user = QUser.user;
        QPermission permission = QPermission.permission;
        QDepartment department = QDepartment.department;

        List<DepartmentPermissionDto.PermissionInfo> permissions = jpaQueryFactory
                .select(Projections.constructor(DepartmentPermissionDto.PermissionInfo.class,
                        permission.departmentId,
                        department.name
                ))
                .from(permission)
                .join(permission.user, user).on(user.userId.eq(userId))
                .join(department).on(department.departmentId.eq(permission.departmentId))
                .fetch();

        return new DepartmentPermissionDto(permissions);
    }
}
