package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Learn how to turn Reactive API to blocking one.
 *
 * @author Sebastien Deleuze
 */
public class Part10ReactiveToBlocking {

//========================================================================================

//	The subscribeOn method allow to isolate a sequence from the start on a provided Scheduler. For example, the Schedulers.elastic() will create a pool
//	of threads that grows on demand, releasing threads that haven't been used in a while automatically.
//	Use that trick to slowly read all users from the blocking repository in the first exercise. Note that you will need to wrap the call to the
//	repository inside a Flux.defer lambda.
	// TODO Return the user contained in that Mono
	User monoToValue(Mono<User> mono) {
		return mono.block();
	}

//========================================================================================

//	For slow subscribers (eg. saving to a database), you can isolate a smaller section of the sequence with the publishOn operator.
//	Unlike subscribeOn, it only affects the part of the chain below it, switching it to a new Scheduler.
//	As an example, you can use doOnNext to perform a save on the repository, but first use the trick above to isolate that save
//	into its own execution context. You can make it more explicit that you're only interested in knowing if the save succeeded or failed
//	by chaining the then() operator at the end, which returns a Mono<Void>.
	// TODO Return the users contained in that Flux
	Iterable<User> fluxToValues(Flux<User> flux) {
		return flux.toIterable();
	}

}
