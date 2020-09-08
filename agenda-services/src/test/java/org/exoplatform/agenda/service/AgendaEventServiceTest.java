/*
 * Copyright (C) 2020 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
*/
package org.exoplatform.agenda.service;

import static org.junit.Assert.*;

import java.time.*;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import org.exoplatform.agenda.constant.EventRecurrenceFrequency;
import org.exoplatform.agenda.model.*;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;

public class AgendaEventServiceTest extends BaseAgendaEventTest {

  @Test
  public void testCreateEvent() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, start, allDay);
    Event createdEvent = createEvent(event.clone(), creatorUserName, testuser2Identity, testuser3Identity);

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    assertEquals(event.getSummary(), createdEvent.getSummary());
    assertEquals(event.getDescription(), createdEvent.getDescription());
    assertEquals(event.getCalendarId(), createdEvent.getCalendarId());
    assertEquals(event.getColor(), createdEvent.getColor());
    assertEquals(event.getStart().toLocalDate(), createdEvent.getStart().toLocalDate());
    assertEquals(event.getEnd().toLocalDate(), createdEvent.getEnd().toLocalDate());
    assertEquals(event.getLocation(), createdEvent.getLocation());
    assertEquals(Long.parseLong(testuser1Identity.getId()), createdEvent.getCreatorId());
    assertEquals(event.getRemoteId(), createdEvent.getRemoteId());
    assertEquals(event.getRemoteProviderId(), createdEvent.getRemoteProviderId());
    assertEquals(event.getAvailability(), createdEvent.getAvailability());
    assertEquals(event.getOccurrence(), createdEvent.getOccurrence());

    assertNotNull(createdEvent.getCreated());
    assertNull(createdEvent.getUpdated());
    assertEquals(0, createdEvent.getModifierId());

    EventRecurrence createdEventRecurrence = createdEvent.getRecurrence();
    assertNotNull(createdEventRecurrence);
    assertTrue(createdEventRecurrence.getId() > 0);

    EventRecurrence eventRecurrence = event.getRecurrence();
    assertEquals(eventRecurrence.getFrequency(), createdEventRecurrence.getFrequency());
    assertEquals(eventRecurrence.getInterval(), createdEventRecurrence.getInterval());
    assertTrue(createdEventRecurrence.getCount() <= 0);
    assertNotNull(createdEventRecurrence.getUntil());
    assertEquals(start.plusDays(2).toLocalDate(),
                 createdEventRecurrence.getUntil().toLocalDate());
    assertEquals(createdEvent.getRecurrence().getOverallEnd().toLocalDate(),
                 createdEventRecurrence.getUntil().toLocalDate());
    assertEquals(eventRecurrence.getBySecond(), createdEventRecurrence.getBySecond());
    assertEquals(eventRecurrence.getByMinute(), createdEventRecurrence.getByMinute());
    assertEquals(eventRecurrence.getByHour(), createdEventRecurrence.getByHour());
    assertEquals(eventRecurrence.getByDay(), createdEventRecurrence.getByDay());
    assertEquals(eventRecurrence.getByMonthDay(), createdEventRecurrence.getByMonthDay());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());
    assertEquals(eventRecurrence.getByMonth(), createdEventRecurrence.getByMonth());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());

    assertNotNull(createdEventRecurrence.getOverallStart());
    assertEquals(start.toLocalDate(),
                 createdEventRecurrence.getOverallStart().toLocalDate());

    assertNotNull(createdEventRecurrence.getOverallEnd());
    assertEquals(start.plusDays(2).toLocalDate(),
                 createdEventRecurrence.getOverallEnd().toLocalDate());
  }

  @Test
  public void testCreateEvent_InSpace_AsMember() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, start, allDay);
    event.setCalendarId(spaceCalendar.getId());
    Event createdEvent = createEvent(event.clone(), creatorUserName, testuser2Identity, testuser3Identity);

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    try {
      event = newEventInstance(start, start, allDay);
      event.setCalendarId(spaceCalendar.getId());
      createEvent(event.clone(), testuser5Identity.getRemoteId(), testuser2Identity, testuser3Identity);
      fail("testuser5 is not member of space and shouldn't be able to create an event");
    } catch (IllegalAccessException e) {
      // Expected
    }
  }

  @Test
  public void testGetEventById_Recurrent() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);
    ZonedDateTime end = ZonedDateTime.now().withNano(0).plusHours(2);

    boolean allDay = false;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, end, allDay);
    Event createdEvent = createEvent(event.clone(), creatorUserName, testuser2Identity);
    createdEvent = agendaEventService.getEventById(createdEvent.getId(), testuser2Identity.getRemoteId());

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    assertEquals(event.getSummary(), createdEvent.getSummary());
    assertEquals(event.getDescription(), createdEvent.getDescription());
    assertEquals(event.getCalendarId(), createdEvent.getCalendarId());
    assertEquals(event.getColor(), createdEvent.getColor());
    assertEquals(event.getLocation(), createdEvent.getLocation());
    assertEquals(Long.parseLong(testuser1Identity.getId()), createdEvent.getCreatorId());
    assertEquals(event.getRemoteId(), createdEvent.getRemoteId());
    assertEquals(event.getRemoteProviderId(), createdEvent.getRemoteProviderId());
    assertEquals(event.getAvailability(), createdEvent.getAvailability());
    assertEquals(event.getOccurrence(), createdEvent.getOccurrence());
    assertEquals(event.getStart().withZoneSameInstant(ZoneOffset.UTC),
                 createdEvent.getStart().withZoneSameInstant(ZoneOffset.UTC));
    assertEquals(event.getEnd().withZoneSameInstant(ZoneOffset.UTC), createdEvent.getEnd().withZoneSameInstant(ZoneOffset.UTC));

    assertNotNull(createdEvent.getCreated());
    assertNull(createdEvent.getUpdated());
    assertEquals(0, createdEvent.getModifierId());

    EventRecurrence createdEventRecurrence = createdEvent.getRecurrence();
    assertNotNull(createdEventRecurrence);
    assertTrue(createdEventRecurrence.getId() > 0);

    EventRecurrence eventRecurrence = event.getRecurrence();
    assertEquals(eventRecurrence.getFrequency(), createdEventRecurrence.getFrequency());
    assertEquals(eventRecurrence.getInterval(), createdEventRecurrence.getInterval());
    assertTrue(createdEventRecurrence.getCount() <= 0);
    assertNotNull(createdEventRecurrence.getUntil());

    assertEquals(end.plusDays(2).toLocalDate(),
                 createdEventRecurrence.getUntil().toLocalDate());
    assertEquals(createdEvent.getRecurrence().getOverallEnd().toLocalDate(),
                 createdEventRecurrence.getUntil().toLocalDate());

    assertEquals(eventRecurrence.getBySecond(), createdEventRecurrence.getBySecond());
    assertEquals(eventRecurrence.getByMinute(), createdEventRecurrence.getByMinute());
    assertEquals(eventRecurrence.getByHour(), createdEventRecurrence.getByHour());
    assertEquals(eventRecurrence.getByDay(), createdEventRecurrence.getByDay());
    assertEquals(eventRecurrence.getByMonthDay(), createdEventRecurrence.getByMonthDay());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());
    assertEquals(eventRecurrence.getByMonth(), createdEventRecurrence.getByMonth());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());

    assertNotNull(createdEventRecurrence.getOverallStart());
    assertEquals(start.withZoneSameInstant(ZoneOffset.UTC),
                 createdEventRecurrence.getOverallStart());

    assertNotNull(createdEventRecurrence.getOverallEnd());
    assertEquals(end.withZoneSameLocal(ZoneOffset.UTC).plusDays(2).toLocalDate(),
                 createdEventRecurrence.getOverallEnd().toLocalDate());
    assertEquals(end.withZoneSameInstant(ZoneOffset.UTC).plusDays(2).getHour(),
                 createdEventRecurrence.getOverallEnd().getHour());
    assertEquals(end.withZoneSameInstant(ZoneOffset.UTC).plusDays(2).getMinute(),
                 createdEventRecurrence.getOverallEnd().getMinute());
    assertEquals(end.withZoneSameInstant(ZoneOffset.UTC).plusDays(2).getSecond(),
                 createdEventRecurrence.getOverallEnd().getSecond());
  }

  @Test
  public void testGetEventById_RecurrenceAttributes() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, start, allDay);
    event.getRecurrence().setFrequency(EventRecurrenceFrequency.YEARLY);
    event.getRecurrence().setBySecond(Collections.singletonList("1"));
    event.getRecurrence().setByMinute(Collections.singletonList("1"));
    event.getRecurrence().setByHour(Collections.singletonList("1"));
    event.getRecurrence().setByDay(Collections.singletonList("TU"));
    event.getRecurrence().setByMonthDay(Collections.singletonList("2"));
    event.getRecurrence().setByMonth(Collections.singletonList("3"));
    event.getRecurrence().setByWeekNo(Collections.singletonList("30"));
    event.getRecurrence().setByYearDay(Collections.singletonList("165"));
    event.getRecurrence().setBySetPos(Collections.singletonList("-1"));
    event = createEvent(event.clone(), creatorUserName, testuser2Identity);

    Event createdEvent = agendaEventService.getEventById(event.getId(), testuser2Identity.getRemoteId());

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    EventRecurrence createdEventRecurrence = createdEvent.getRecurrence();
    assertNotNull(createdEventRecurrence);
    assertTrue(createdEventRecurrence.getId() > 0);

    EventRecurrence eventRecurrence = event.getRecurrence();
    assertTrue(createdEventRecurrence.getCount() <= 0);
    assertEquals(eventRecurrence.getFrequency(), createdEventRecurrence.getFrequency());
    assertEquals(eventRecurrence.getInterval(), createdEventRecurrence.getInterval());
    assertEquals(eventRecurrence.getBySecond(), createdEventRecurrence.getBySecond());
    assertEquals(eventRecurrence.getByMinute(), createdEventRecurrence.getByMinute());
    assertEquals(eventRecurrence.getByHour(), createdEventRecurrence.getByHour());
    assertEquals(eventRecurrence.getByDay(), createdEventRecurrence.getByDay());
    assertEquals(eventRecurrence.getByMonthDay(), createdEventRecurrence.getByMonthDay());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());
    assertEquals(eventRecurrence.getByMonth(), createdEventRecurrence.getByMonth());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());
  }

  @Test
  public void testGetEventById_Recurrent_AllDayEvent() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, start, allDay);
    event = createEvent(event.clone(), creatorUserName, testuser2Identity);

    try {
      agendaEventService.getEventById(event.getId(), testuser3Identity.getRemoteId());
      fail("Should fail when a non attendee attempts to access event");
    } catch (IllegalAccessException e) {
      // Expected
    }

    Event createdEvent = agendaEventService.getEventById(event.getId(), testuser2Identity.getRemoteId());

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    assertEquals(event.getSummary(), createdEvent.getSummary());
    assertEquals(event.getDescription(), createdEvent.getDescription());
    assertEquals(event.getCalendarId(), createdEvent.getCalendarId());
    assertEquals(event.getColor(), createdEvent.getColor());
    assertEquals(event.getLocation(), createdEvent.getLocation());
    assertEquals(event.getCreatorId(), createdEvent.getCreatorId());
    assertEquals(event.getRemoteId(), createdEvent.getRemoteId());
    assertEquals(event.getRemoteProviderId(), createdEvent.getRemoteProviderId());
    assertEquals(event.getAvailability(), createdEvent.getAvailability());
    assertEquals(event.getOccurrence(), createdEvent.getOccurrence());
    assertEquals(event.getStart().withZoneSameLocal(ZoneOffset.UTC), createdEvent.getStart().withZoneSameLocal(ZoneOffset.UTC));
    assertEquals(event.getEnd().withZoneSameLocal(ZoneOffset.UTC), createdEvent.getEnd().withZoneSameLocal(ZoneOffset.UTC));

    assertNotNull(createdEvent.getCreated());
    assertNull(createdEvent.getUpdated());
    assertEquals(0, createdEvent.getModifierId());

    EventRecurrence createdEventRecurrence = createdEvent.getRecurrence();
    assertNotNull(createdEventRecurrence);
    assertTrue(createdEventRecurrence.getId() > 0);

    EventRecurrence eventRecurrence = event.getRecurrence();
    assertEquals(eventRecurrence.getFrequency(), createdEventRecurrence.getFrequency());
    assertEquals(eventRecurrence.getInterval(), createdEventRecurrence.getInterval());
    assertTrue(createdEventRecurrence.getCount() <= 0);
    assertEquals(start.plusDays(2).toLocalDate(),
                 createdEventRecurrence.getUntil().toLocalDate());
    assertEquals(createdEvent.getRecurrence().getOverallEnd().toLocalDate(),
                 createdEventRecurrence.getUntil().toLocalDate());
    assertEquals(eventRecurrence.getBySecond(), createdEventRecurrence.getBySecond());
    assertEquals(eventRecurrence.getByMinute(), createdEventRecurrence.getByMinute());
    assertEquals(eventRecurrence.getByHour(), createdEventRecurrence.getByHour());
    assertEquals(eventRecurrence.getByDay(), createdEventRecurrence.getByDay());
    assertEquals(eventRecurrence.getByMonthDay(), createdEventRecurrence.getByMonthDay());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());
    assertEquals(eventRecurrence.getByMonth(), createdEventRecurrence.getByMonth());
    assertEquals(eventRecurrence.getByYearDay(), createdEventRecurrence.getByYearDay());

    assertNotNull(createdEventRecurrence.getOverallStart());
    assertEquals(start.toLocalDate().atStartOfDay(ZoneOffset.UTC),
                 createdEventRecurrence.getOverallStart());

    assertNotNull(createdEventRecurrence.getOverallEnd());
    assertEquals(start.plusDays(2).toLocalDate(),
                 createdEventRecurrence.getOverallEnd().toLocalDate());
  }

  @Test
  public void testUpdateEvent() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, start, allDay);
    event = createEvent(event.clone(), creatorUserName, testuser2Identity);

    long eventId = event.getId();
    Event createdEvent = agendaEventService.getEventById(eventId, testuser2Identity.getRemoteId());

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);
    assertNotNull(createdEvent.getRecurrence());

    createdEvent.setRecurrence(null);
    createdEvent.setRemoteProviderId(0);

    agendaEventService.updateEvent(createdEvent, null, null, null, null, false, testuser1Identity.getRemoteId());

    Event updatedEvent = agendaEventService.getEventById(eventId, testuser1Identity.getRemoteId());
    assertNotNull(updatedEvent);
    assertNull(updatedEvent.getRecurrence());
    assertNull(updatedEvent.getOccurrence());
    assertEquals(0, updatedEvent.getRemoteProviderId());

    List<EventAttachment> eventAttachments = agendaEventAttachmentService.getEventAttachments(eventId);
    assertTrue(eventAttachments == null || eventAttachments.isEmpty());
    List<EventAttendee> eventAttendees = agendaEventAttendeeService.getEventAttendees(eventId);
    assertTrue(eventAttendees == null || eventAttendees.isEmpty());
    List<EventConference> eventConferences = agendaEventConferenceService.getEventConferences(eventId);
    assertTrue(eventConferences == null || eventConferences.isEmpty());

    updatedEvent.setAllowAttendeeToUpdate(true);
    updatedEvent.setAllowAttendeeToInvite(false);

    EventAttendee eventAttendee = new EventAttendee(0, Long.parseLong(testuser2Identity.getId()), null);
    updatedEvent = agendaEventService.updateEvent(updatedEvent,
                                                  Collections.singletonList(eventAttendee),
                                                  null,
                                                  null,
                                                  null,
                                                  false,
                                                  testuser1Identity.getRemoteId());
    assertTrue(updatedEvent.isAllowAttendeeToUpdate());
    assertTrue("allowAttendeeToInvite should be true automatically when allowAttendeeToUpdate is set to true",
               updatedEvent.isAllowAttendeeToInvite());

    try {
      updatedEvent = agendaEventService.updateEvent(updatedEvent,
                                                    Collections.singletonList(eventAttendee),
                                                    null,
                                                    null,
                                                    null,
                                                    false,
                                                    testuser3Identity.getRemoteId());
    } catch (IllegalAccessException e) {
      // Expected
    }

    updatedEvent.setAllowAttendeeToUpdate(false);
    updatedEvent = agendaEventService.updateEvent(updatedEvent,
                                                  Collections.singletonList(eventAttendee),
                                                  null,
                                                  null,
                                                  null,
                                                  false,
                                                  testuser2Identity.getRemoteId());
    assertTrue("Attendees shouldn't be able to modify allowAttendeeToInvite and allowAttendeeToUpdate",
               updatedEvent.isAllowAttendeeToUpdate());
    assertTrue(updatedEvent.isAllowAttendeeToInvite());
  }

  @Test
  public void testUpdateEvent_InSpace_AsMember() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, start, allDay);
    event.setCalendarId(spaceCalendar.getId());
    Event createdEvent = createEvent(event.clone(), creatorUserName, testuser2Identity, testuser3Identity);

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    String newDescription = "Desc2";
    createdEvent.setDescription(newDescription);
    agendaEventService.updateEvent(createdEvent, null, null, null, null, false, testuser1Identity.getRemoteId());

    Event updatedEvent = agendaEventService.getEventById(createdEvent.getId(), testuser1Identity.getRemoteId());

    assertNotNull(updatedEvent);
    assertEquals(newDescription, updatedEvent.getDescription());

    spaceService.removeMember(space, testuser1Identity.getRemoteId());
    try {
      agendaEventService.updateEvent(updatedEvent, null, null, null, null, false, testuser1Identity.getRemoteId());
      fail("testuser1 shouldn't be able to update a previously created event by him, while he's not member of space anymore");
    } catch (IllegalAccessException e) {
      // Expected
    } finally {
      spaceService.addMember(space, testuser1Identity.getRemoteId());
    }
  }

  @Test
  public void testDeleteEvent() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, start, allDay);
    event = createEvent(event.clone(), creatorUserName, testuser2Identity);

    long eventId = event.getId();
    try {
      agendaEventService.deleteEventById(eventId, testuser2Identity.getRemoteId());
      fail("Event with id " + eventId + " shouldn't be deletable by an attendee");
    } catch (IllegalAccessException e) {
      // Expected to have this exception, just check if event really always
      // exists
      event = agendaEventService.getEventById(eventId, testuser1Identity.getRemoteId());
      assertNotNull(event);
    }

    agendaEventService.deleteEventById(eventId, testuser1Identity.getRemoteId());

    event = agendaEventService.getEventById(eventId, testuser1Identity.getRemoteId());
    assertNull(event);

    List<EventAttachment> eventAttachments = agendaEventAttachmentService.getEventAttachments(eventId);
    assertTrue(eventAttachments == null || eventAttachments.isEmpty());
    List<EventAttendee> eventAttendees = agendaEventAttendeeService.getEventAttendees(eventId);
    assertTrue(eventAttendees == null || eventAttendees.isEmpty());
    List<EventConference> eventConferences = agendaEventConferenceService.getEventConferences(eventId);
    assertTrue(eventConferences == null || eventConferences.isEmpty());
  }

  @Test
  public void testGetEvents_Recurrent_WithExceptionalEvent() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);
    ZonedDateTime end = ZonedDateTime.now().plusHours(2).withNano(0);

    boolean allDay = false;
    String creatorUserName = testuser1Identity.getRemoteId();

    Event event = newEventInstance(start, end, allDay);
    event = createEvent(event.clone(), creatorUserName, testuser2Identity);
    Event createdEvent = agendaEventService.getEventById(event.getId(), testuser2Identity.getRemoteId());

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    List<Event> events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                                      ZonedDateTime.now().plusMinutes(90),
                                                      testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());
    assertEquals(0, events.get(0).getId());

    Event exceptionalEvent = events.get(0).clone();
    exceptionalEvent = createEvent(exceptionalEvent, creatorUserName, testuser2Identity);

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                          ZonedDateTime.now().plusMinutes(90),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());
    assertTrue(events.get(0).getId() > 0);
    assertEquals(exceptionalEvent.getId(), events.get(0).getId());

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1).plusDays(1),
                                          ZonedDateTime.now().plusMinutes(90).plusDays(1),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());
    assertEquals(0, events.get(0).getId());

    exceptionalEvent.setEnd(exceptionalEvent.getEnd().plusDays(1));
    exceptionalEvent.setStart(exceptionalEvent.getStart().plusDays(1));
    agendaEventService.updateEvent(exceptionalEvent, ATTENDEES, null, null, null, false, testuser1Identity.getRemoteId());

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                          ZonedDateTime.now().plusMinutes(90),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(0, events.size());

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1).plusDays(1),
                                          ZonedDateTime.now().plusMinutes(90).plusDays(1),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(2, events.size());
  }

  @Test
  public void testGetEvents_SpaceMembers() throws Exception { // NOSONAR
    ZonedDateTime start = ZonedDateTime.now().withNano(0);

    boolean allDay = true;
    String creatorUserName = testuser1Identity.getRemoteId();

    Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName());

    Event event = newEventInstance(start, start, allDay);
    event = createEvent(event.clone(), creatorUserName, spaceIdentity);
    Event createdEvent = agendaEventService.getEventById(event.getId(), testuser2Identity.getRemoteId());

    assertNotNull(createdEvent);
    assertTrue(createdEvent.getId() > 0);

    try {
      agendaEventService.getEventById(event.getId(), testuser4Identity.getRemoteId());
      fail("Should throw an exception when a non member user attempts to access a space event");
    } catch (IllegalAccessException e) {
      // Expected
    }

    List<Event> events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                                      ZonedDateTime.now().plusMinutes(90),
                                                      testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                          ZonedDateTime.now().plusMinutes(90),
                                          testuser4Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(0, events.size());
  }

  @Test
  public void testGetEvents() throws Exception { // NOSONAR
    ZonedDateTime now = ZonedDateTime.now();

    ZonedDateTime start = now.withNano(0);
    ZonedDateTime end = start.plusHours(2);

    Event event = newEventInstance(start, end, false);
    Event event1 = createEvent(event.clone(), testuser1Identity.getRemoteId(), testuser2Identity, testuser3Identity);
    event1 = agendaEventService.getEventById(event1.getId(), testuser1Identity.getRemoteId());

    List<Event> events = agendaEventService.getEvents(start, end, testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());

    Event occurrenceEvent = events.get(0);
    assertEquals(0, occurrenceEvent.getId());
    assertEquals(event1.getId(), occurrenceEvent.getParentId());

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                          ZonedDateTime.now().plusMinutes(90),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());

    assertEquals(0, occurrenceEvent.getId());
    assertEquals(event1.getId(), occurrenceEvent.getParentId());

    event = newEventInstance(start, start, true);
    createEvent(event.clone(), testuser1Identity.getRemoteId(), testuser2Identity);

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                          ZonedDateTime.now().plusMinutes(90),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(2, events.size());

    events = agendaEventService.getEvents(ZonedDateTime.now().plusHours(1),
                                          ZonedDateTime.now().plusMinutes(90).plusDays(1),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(4, events.size());

    events = agendaEventService.getEvents(event1.getRecurrence()
                                                .getUntil()
                                                .plusDays(2)
                                                .toLocalDate()
                                                .atStartOfDay(ZoneId.systemDefault()),
                                          event1.getRecurrence().getUntil().plusDays(3),
                                          testuser2Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(0, events.size());
  }

  @Test
  public void testGetEventsByOwner() throws Exception { // NOSONAR
    ZonedDateTime now = ZonedDateTime.now();

    ZonedDateTime start = now.withNano(0);
    ZonedDateTime end = start.plusHours(2);

    long testuser1Id = Long.parseLong(testuser1Identity.getId());
    try {
      agendaEventService.getEventsByOwners(Collections.singletonList(testuser1Id),
                                           ZonedDateTime.now().plusHours(1),
                                           ZonedDateTime.now().plusMinutes(90),
                                           testuser2Identity.getRemoteId());
      fail("User 'testuser2' shouldn't be able to access calendar of user 'testuser1'");
    } catch (IllegalAccessException e) {
      // Expected
    }

    Event event = newEventInstance(start, end, false);
    Event event1 = createEvent(event.clone(), testuser1Identity.getRemoteId(), testuser1Identity, testuser2Identity);
    event1 = agendaEventService.getEventById(event1.getId(), testuser1Identity.getRemoteId());

    List<Event> events = agendaEventService.getEventsByOwners(Collections.singletonList(testuser1Id),
                                                              ZonedDateTime.now().plusHours(1),
                                                              ZonedDateTime.now().plusMinutes(90),
                                                              testuser1Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());

    Event occurrenceEvent = events.get(0);
    assertEquals(0, occurrenceEvent.getId());
    assertEquals(event1.getId(), occurrenceEvent.getParentId());

    events = agendaEventService.getEventsByOwners(Collections.singletonList(testuser1Id),
                                                  ZonedDateTime.now().plusHours(1),
                                                  ZonedDateTime.now().plusMinutes(90),
                                                  testuser1Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(1, events.size());

    assertEquals(0, occurrenceEvent.getId());
    assertEquals(event1.getId(), occurrenceEvent.getParentId());

    event = newEventInstance(start, start, true);
    createEvent(event.clone(), testuser1Identity.getRemoteId(), testuser1Identity, testuser2Identity);

    events = agendaEventService.getEventsByOwners(Collections.singletonList(testuser1Id),
                                                  ZonedDateTime.now().plusHours(1),
                                                  ZonedDateTime.now().plusMinutes(90),
                                                  testuser1Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(2, events.size());

    events = agendaEventService.getEventsByOwners(Collections.singletonList(testuser1Id),
                                                  ZonedDateTime.now().plusHours(1),
                                                  ZonedDateTime.now().plusMinutes(90).plusDays(1),
                                                  testuser1Identity.getRemoteId());
    assertNotNull(events);
    assertEquals(4, events.size());
  }

}