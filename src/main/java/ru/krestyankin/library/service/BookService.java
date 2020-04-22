package ru.krestyankin.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.repositories.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final MutableAclService aclService;

    public Book save(Book book){
        final Sid owner = new PrincipalSid(SecurityContextHolder.getContext().getAuthentication());
        final Sid admin = new GrantedAuthoritySid("ROLE_ADMIN");
        final Sid reader = new GrantedAuthoritySid("ROLE_READER");

        book=bookRepository.save(book);

        final ObjectIdentity bookIdentity = new ObjectIdentityImpl(Book.class, book.getId());
        MutableAcl acl = aclService.createAcl(bookIdentity);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, admin, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, reader, true);
        aclService.updateAcl(acl);
        return book;
    }

    @Secured("ROLE_ADMIN")
    public void deleteById(String bookId) {
        final ObjectIdentity bookIdentity = new ObjectIdentityImpl(Book.class, bookId);
        MutableAcl acl = (MutableAcl) aclService.readAclById(bookIdentity);
        final List<Sid> sids = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthoritySid::new).collect(Collectors.toList());
        try {
            if (!acl.isGranted(List.of(BasePermission.ADMINISTRATION), sids, false)) {
                throw new RuntimeException("Access denied");
            }
        }
        catch (NotFoundException ignore) {
            throw new RuntimeException("Access denied");
        }
        bookRepository.deleteById(bookId);
        aclService.deleteAcl(bookIdentity, true);
    }
}
