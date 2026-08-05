<script setup lang="ts">
import { chatMessage } from "#build/ui";
import type { NavigationMenuItem } from "@nuxt/ui";

// const route = useRoute();
const toast = useToast();

const open = ref(false);

const links = [
  [
    {
      label: "Home",
      icon: "i-lucide-house",
      to: "/",
      onSelect: () => {
        open.value = false;
      },
    },
    // {
    //   label: "Inbox",
    //   icon: "i-lucide-inbox",
    //   to: "/inbox",
    //   badge: "4",
    //   onSelect: () => {
    //     open.value = false;
    //   },
    // },
    // {
    //   label: "Customers",
    //   icon: "i-lucide-users",
    //   to: "/customers",
    //   onSelect: () => {
    //     open.value = false;
    //   },
    // },
    {
      label: "Configurações",
      to: "/settings",
      icon: "i-lucide-settings",
      defaultOpen: true,
      type: "trigger",
      children: [
        {
          label: "Geral",
          to: "/settings",
          exact: true,
          onSelect: () => {
            open.value = false;
          },
        },
        {
          label: "Membros",
          to: "/settings/members",
          onSelect: () => {
            open.value = false;
          }
        },
        {
          label: "Segurança",
          to: "/settings/security",
          onSelect: () => {
            open.value = false
          }
        }
      ]
    }
  ]
] satisfies NavigationMenuItem[][]

const groups = computed(() => [
  {
    id: "links",
    label: "Go to",
    items: links.flat()
  }
])

const user = ref({
  name: 'Caio Ximenes',
  avatar: {
    src: 'https://avatars.githubusercontent.com/u/80227027?v=4&size=64',
    alt: 'Benjamin Canac'
  }
})

onMounted(async () => {
  const cookie = useCookie("cookie-consent");
  if (cookie.value === "accepted") {
    return;
  }

  toast.add({
    title:
      "Usamos cookies próprios para melhorar sua experiência em nosso site.",
    duration: 0,
    close: false,
    actions: [
      {
        label: "Aceitar",
        color: "neutral",
        variant: "outline",
        onClick: () => {
          cookie.value = "accepted";
        },
      },
      {
        label: "Recusar",
        color: "neutral",
        variant: "ghost",
      },
    ],
  });
});
</script>

<template>
  <UDashboardGroup unit="rem">
    <UDashboardSidebar
      id="default"
      v-model:open="open"
      collapsible
      resizable
      class="bg-elevated/25"
      :ui="{ footer: 'lg:border-t lg:border-default' }"
    >

      <template #default="{ collapsed }">
        <UDashboardSearchButton
          :collapsed="collapsed"
          class="bg-transparent ring-default"
        />

        <UNavigationMenu
          :collapsed="collapsed"
          :items="links[0]"
          orientation="vertical"
          tooltip
          popover
        />

        <!-- <UNavigationMenu
          :collapsed="collapsed"
          :items="links[1]"
          orientation="vertical"
          tooltip
          class="mt-auto"
        /> -->
      </template>

      <template #footer>
        <UButton
          v-bind="user"
          :label="user.name"
          trailing-icon="i-lucide-chevrons-up-down"
          color="neutral"
          variant="ghost"
          square
          class="w-full data-[state=open]:bg-elevated overflow-hidden"
          :ui="{
            trailingIcon: 'text-dimmed ms-auto'
          }"
        />
      </template>
    </UDashboardSidebar>

    <UDashboardSearch :groups="groups" />

    <slot />

    <!-- <DashboardNotificationsSlideover /> -->
  </UDashboardGroup>
</template>
