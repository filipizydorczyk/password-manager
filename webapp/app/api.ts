const BASE_URL = "http://localhost:3001";

export const getPasswordsList = async (pin: string): Promise<string[]> => {
  const res: any = await $fetch(`${BASE_URL}/get`, {
    method: "GET",
    headers: { Authorization: pin },
  }).catch(() => undefined);

  return (res?.result || []) as string[];
};

export const getPasswordByName = async (pin: string, name: string): Promise<string> => {
  const res: any = await $fetch(`${BASE_URL}/get?name=${name}`, {
    method: "GET",
    headers: { Authorization: pin },
  }).catch(() => undefined);

  return (res?.result || []) as string;
};

export const getPINLength = async (): Promise<number> => {
  const res: any = await $fetch(`${BASE_URL}/pin/len`, {
    method: "GET",
  }).catch(() => undefined);

  return (res?.result || []) as number;
};

export const verifyPin = async (pin: string): Promise<boolean> => {
  const res: any = await $fetch(`${BASE_URL}/pin/verify?pin=${pin}`, {
    method: "GET",
  }).catch(() => undefined);

  return (Boolean(res?.result) || false) as boolean;
};

export const addAPIPassword = async (pin: string, name: string, password: string) => {
  await $fetch(`${BASE_URL}/add`, {
    method: "POST",
    headers: { Authorization: pin },
    body: { name, password },
  }).catch(() => undefined);
};

export const delPassword = async (pin: string, name: string) => {
  await $fetch(`${BASE_URL}/del?name=${name}`, {
    method: "DELETE",
    headers: { Authorization: pin },
  }).catch(() => undefined);
};
