package controllers.AccountantControllers.new_book;

import Database.Repository.BookProposalRepository;
import controllers.LoginController;
import javafx.scene.control.TableView;
import models.entities.BookProposal;
import models.tables.BookProposalTable;


import java.util.List;

public class NewBookProposalService {

    private final BookProposalRepository accProposalRepository;
    private static NewBookProposalService INSTANCE;

    private NewBookProposalService() {
        accProposalRepository = BookProposalRepository.getInstance();
    }

    public static NewBookProposalService getInstance() {
        if(INSTANCE == null){
            INSTANCE = new NewBookProposalService();
        }
        return INSTANCE;
    }

    public List<BookProposalTable> getProposal() {
        return accProposalRepository.getTableProposals();
    }

    public List<BookProposal> getFullProposal() {
        return accProposalRepository.getFullProposals();
    }

    public void confirList(TableView<BookProposalTable> newProposalBookTable) {
        List<BookProposal> fullProposals = getFullProposal();
        newProposalBookTable.getItems().forEach(el ->{
            fullProposals.stream()
                    .filter(el1 -> el1.getTitle().equals(el.getTitle()) && el1.getAuthor().equals(el.getAuthor()))
                    .findAny()
                    .ifPresentOrElse(el2 ->{
                                el2.setPrice(Double.parseDouble(el.getPrice()));
                                el2.setQuantity(Integer.parseInt(el.getQuantity()));
                                accProposalRepository.updateBookProposal(el2);
                            },
                            () -> {
                                BookProposal bookProposal = new BookProposal();
                                bookProposal.setAuthor(el.getAuthor());
                                bookProposal.setStatus(BookProposal.Status.PROPOSAL.toString());
                                bookProposal.setTitle(el.getTitle());
                                bookProposal.setPrice(Double.parseDouble(el.getPrice()));
                                bookProposal.setQuantity(Integer.parseInt(el.getQuantity()));
                                bookProposal.setPerson(LoginController.loggedUser);
                                accProposalRepository.createBookProposal(bookProposal);
                            });

        });
    }
}
