package br.com.fiap.postech.domain.owner.usecase;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.owner.exception.DuplicatedOwnerException;
import br.com.fiap.postech.domain.owner.exception.NoMatchingOwnersException;
import br.com.fiap.postech.domain.owner.exception.OwnerNotFoundException;
import br.com.fiap.postech.domain.owner.model.Owner;
import br.com.fiap.postech.port.persistence.owner.OwnerPersistencePort;

public class OwnerUseCase {
    private final OwnerPersistencePort persistencePort;

    public OwnerUseCase(OwnerPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public ScrollPage<Owner> scroll(String email, Integer pageSize, String cursor) {
        final var result = persistencePort.scroll(email, pageSize, cursor);

        if (result.data().isEmpty()) {
            throw new NoMatchingOwnersException(email);
        }

        return result;
    }

    public Owner getById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
    }

    public Owner create(Owner owner) {
        persistencePort.findByDocument(owner.getDocument()).ifPresent(s -> {
            throw new DuplicatedOwnerException(owner.getDocument());
        });

        return persistencePort.save(owner);
    }

    public Owner update(Long id, Owner owner) {
        persistencePort.findByDocument(owner.getDocument()).ifPresent(s -> {
            throw new DuplicatedOwnerException(owner.getDocument());
        });

        persistencePort.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
        owner.setId(id);

        return persistencePort.save(owner);
    }

    public void delete(Long id) {
        if (!persistencePort.existsById(id)) {
            throw new OwnerNotFoundException(id);
        }

        persistencePort.deleteById(id);
    }
}
