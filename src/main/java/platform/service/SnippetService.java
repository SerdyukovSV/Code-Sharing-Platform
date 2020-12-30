package platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import platform.repository.SnippetRepository;

@Service
public class SnippetService {

    @Autowired
    private SnippetRepository repository;

    public Page getAllSnippet() {

        Pageable paging = PageRequest.of(0, 10, Sort.by("date").descending());

        return repository.findAllByRestrictTimeAndRestrictView(paging, false, false);
    }
}
