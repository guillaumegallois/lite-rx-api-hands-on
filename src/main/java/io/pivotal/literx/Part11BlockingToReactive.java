package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.BlockingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Learn how to call blocking code from Reactive one with adapted concurrency strategy for
 * blocking code that produces or receives data.
 *
 * For those who know RxJava:
 *  - RxJava subscribeOn = Reactor subscribeOn
 *  - RxJava observeOn = Reactor publishOn
 *  - RxJava Schedulers.io <==> Reactor Schedulers.elastic
 *
 * @author Sebastien Deleuze
 * @see Flux#subscribeOn(Scheduler)
 * @see Flux#publishOn(Scheduler)
 * @see Schedulers
 */
public class Part11BlockingToReactive {

//========================================================================================

//	Sometimes you can only migrate part of your code to be reactive, and you need to reuse reactive sequences in more imperative code.
//	Thus if you need to block until the value from a Mono is available, use Mono#block() method. It will throw an Exception if the onError event
//	is triggered.
//	Note that you should avoid this by favoring having reactive code end-to-end, as much as possible. You MUST avoid this at all cost in the middle
//	of other reactive code, as this has the potential to lock your whole reactive pipeline.
	// TODO Create a Flux for reading all users from the blocking repository deferred until the flux is subscribed, and run it with an elastic scheduler
	Flux<User> blockingRepositoryToFlux(BlockingRepository<User> repository) {
		return Flux.defer(() -> Flux.fromIterable(repository.findAll()))
				.subscribeOn(Schedulers.elastic());
	}

//========================================================================================

//	Similarly, you can block for the first or last value in a Flux with blockFirst()/blockLast().
//	You can also transform a Flux to an Iterable with toIterable.
//	Same restrictions as above still apply.
	// TODO Insert users contained in the Flux parameter in the blocking repository using an elastic scheduler and return a Mono<Void>
	// that signal the end of the operation
	Mono<Void> fluxToBlockingRepository(Flux<User> flux, BlockingRepository<User> repository) {
		return flux
				.publishOn(Schedulers.elastic())
				.doOnNext(repository::save)
				.then();
	}

}
