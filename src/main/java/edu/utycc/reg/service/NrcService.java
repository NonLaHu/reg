package edu.utycc.reg.service;

import edu.utycc.reg.entity.NrcState;
import edu.utycc.reg.entity.NrcTownship;
import edu.utycc.reg.entity.NrcType;
import edu.utycc.reg.repository.NrcStateRepository;
import edu.utycc.reg.repository.NrcTownshipRepository;
import edu.utycc.reg.repository.NrcTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NrcService {

    private final NrcStateRepository stateRepo;
    private final NrcTownshipRepository townshipRepo;
    private final NrcTypeRepository typeRepo;

    public NrcService(NrcStateRepository stateRepo,
                      NrcTownshipRepository townshipRepo,
                      NrcTypeRepository typeRepo) {
        this.stateRepo = stateRepo;
        this.townshipRepo = townshipRepo;
        this.typeRepo = typeRepo;
    }

    public List<NrcState> getAllStates() {
        return stateRepo.findAll();
    }

    public List<NrcTownship> getAllTownships() {
        return townshipRepo.findAll();
    }

    public List<NrcTownship> getTownshipsByStateId(Short stateId) {
        return townshipRepo.findByStateId(stateId);
    }

    public List<NrcType> getAllTypes() {
        return typeRepo.findAll();
    }
}
