package edu.utycc.reg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.utycc.reg.dto.RoleDto;
import edu.utycc.reg.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleDto(role.getId(), role.getName(), role.getDescription()))
                .collect(Collectors.toList());
    }
}
