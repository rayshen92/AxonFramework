/*
 * Copyright (c) 2010-2020. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.messaging;

import reactor.core.publisher.Flux;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ResultHandler<M extends Message<?>, R extends Message<?>> {

    void onResult(M message, R result);

    void onComplete(M message);

    void onError(M message, Throwable error);

    default ResultHandler<M, R> andOnError(BiConsumer<M, Throwable> onError) {
        return new ResultHandlerAdapter<M, R>(this) {
            @Override
            public void onError(M message, Throwable error) {
                onError.accept(message, error);
                super.onError(message, error);
            }
        };
    };

    default ResultHandler<M, R> andOnResult(BiConsumer<M, R> onResult) {
        return new ResultHandlerAdapter<M, R>(this) {

            @Override
            public void onResult(M message, R result) {
                onResult.accept(message, result);
                super.onResult(message, result);
            }
        };
    };

    default ResultHandler<M, R> andOnComplete(Consumer<M> onComplete) {
        return new ResultHandlerAdapter<M, R>(this) {
            @Override
            public void onComplete(M message) {
                onComplete.accept(message);
                super.onComplete(message);
            }

        };
    };
}