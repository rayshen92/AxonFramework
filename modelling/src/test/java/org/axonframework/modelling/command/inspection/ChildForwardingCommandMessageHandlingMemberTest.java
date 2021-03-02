package org.axonframework.modelling.command.inspection;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.HandlerAttributes;
import org.axonframework.messaging.annotation.MessageHandlingMember;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class validating the {@link ChildForwardingCommandMessageHandlingMember}.
 *
 * @author Steven van Beelen
 */
class ChildForwardingCommandMessageHandlingMemberTest {

    private MessageHandlingMember<Object> childMember;

    private ChildForwardingCommandMessageHandlingMember<Object, Object> testSubject;

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        childMember = mock(MessageHandlingMember.class);

        testSubject = new ChildForwardingCommandMessageHandlingMember<>(
                Collections.emptyList(), childMember, (msg, parent) -> parent
        );
    }

    @Test
    void testCanHandleMessageTypeIsDelegatedToChildHandler() {
        when(childMember.canHandleMessageType(any())).thenReturn(true);

        assertTrue(testSubject.canHandleMessageType(CommandMessage.class));

        verify(childMember).canHandleMessageType(CommandMessage.class);
    }

    @Test
    void testAttributeIsDelegatedToChildHandler() {
        AggregateCreationPolicy expectedPolicy = AggregateCreationPolicy.NEVER;
        when(childMember.attribute(HandlerAttributes.AGGREGATE_CREATION_POLICY))
                .thenReturn(Optional.of(expectedPolicy));

        Optional<Object> result = testSubject.attribute(HandlerAttributes.AGGREGATE_CREATION_POLICY);

        assertTrue(result.isPresent());
        assertEquals(expectedPolicy, result.get());

        verify(childMember).attribute(HandlerAttributes.AGGREGATE_CREATION_POLICY);
    }
}