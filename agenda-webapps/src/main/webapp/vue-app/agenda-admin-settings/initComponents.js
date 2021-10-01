// SPDX-FileCopyrightText: 2021 eXo Platform SAS
//
// SPDX-License-Identifier: AGPL-3.0-only

import AgendaAdminSettings from './components/AgendaAdminSettings.vue';
import AgendaAdminConnectorSettings from './components/AgendaAdminConnectorSettings.vue';
import AgendaAdminConferenceSettings from './components/AgendaAdminConferenceSettings.vue';

const components = {
  'agenda-admin-settings': AgendaAdminSettings,
  'agenda-admin-connector-settings': AgendaAdminConnectorSettings,
  'agenda-admin-conference-settings': AgendaAdminConferenceSettings,
};

for (const key in components) {
  Vue.component(key, components[key]);
}
