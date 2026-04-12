import { addAPIPassword, delPassword, getPINLength, getPasswordByName, getPasswordsList, verifyPin } from "./api";
import type { PasswordEntity } from "./types";

export const useStore = defineStore("password-manager-store", () => {
  const pin = ref<string>();
  const pinLength = ref(0);

  const passwords = ref<PasswordEntity[]>([]);

  const getPin = () => {
    return pin;
  };
  const setPin = async (val: string) => {
    const isPinCorrect = await verifyPin(val);

    if (!isPinCorrect) {
      console.log("kurwa");
      return undefined;
    }

    pin.value = val;
    fetchPasswords();
    return pin.value;
  };

  const getPinLength = () => {
    return pinLength;
  };

  const fetchPinLength = async () => {
    const len = await getPINLength();
    pinLength.value = len;
  };

  const getPasswords = () => {
    return passwords;
  };

  const getPasswordSecretByName = async (name: string) => {
    if (!pin.value) {
      return;
    }
    const response = await getPasswordByName(pin.value, name);
    return response;
  };

  const addPassword = async (name: string, password: string) => {
    if (!pin.value) {
      return;
    }
    await addAPIPassword(pin.value, name, password);
    await fetchPasswords();
  };

  const removePassword = async (name: string) => {
    if (!pin.value) {
      return;
    }
    await delPassword(pin.value, name);
    await fetchPasswords();
  };

  const fetchPasswords = async () => {
    if (!pin.value) {
      return;
    }
    const response = await getPasswordsList(pin.value);
    passwords.value = response.map((pas) => ({ name: pas, password: "" }));
    // passwords.value = [
    //   {
    //     name: "Google",
    //     password: "google_secure_password_123",
    //   },
    //   {
    //     name: "GitHub",
    //     password: "gh_token_xyz_987654321",
    //   },
    //   {
    //     name: "Netflix",
    //     password: "netflix_pass_2024!",
    //   },
    //   {
    //     name: "Slack",
    //     password: "slack_workspace_secret",
    //   },
    //   {
    //     name: "Amazon",
    //     password: "prime_delivery_pwd_99",
    //   },
    // ];
  };

  return {
    getPin,
    setPin,
    getPinLength,
    fetchPinLength,
    getPasswordSecretByName,
    getPasswords,
    addPassword,
    removePassword,
    fetchPasswords,
  };
});
