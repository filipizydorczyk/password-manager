const getBaseUrl = () => {
  const config = useRuntimeConfig();
  const baseurl = config.public.apiBase || "http://localhost:3001";

  return baseurl;
};

export const getPasswordsList = async (pin: string): Promise<string[]> => {
  const res: any = await $fetch(`${getBaseUrl()}/get`, {
    method: "GET",
    headers: { Authorization: pin },
  }).catch(() => undefined);

  return (res?.result || []) as string[];
};

export const getPasswordByName = async (pin: string, name: string): Promise<string> => {
  const res: any = await $fetch(`${getBaseUrl()}/get?name=${name}`, {
    method: "GET",
    headers: { Authorization: pin },
  }).catch(() => undefined);

  return (res?.result || []) as string;
};

export const getPINLength = async (): Promise<number> => {
  const res: any = await $fetch(`${getBaseUrl()}/pin/len`, {
    method: "GET",
  }).catch(() => undefined);

  return (res?.result || []) as number;
};

export const verifyPin = async (pin: string): Promise<boolean> => {
  const res: any = await $fetch(`${getBaseUrl()}/pin/verify?pin=${pin}`, {
    method: "GET",
  }).catch(() => undefined);

  return (Boolean(res?.result) || false) as boolean;
};

export const addAPIPassword = async (pin: string, name: string, password: string) => {
  await $fetch(`${getBaseUrl()}/add`, {
    method: "POST",
    headers: { Authorization: pin },
    body: { name, password },
  }).catch(() => undefined);
};

export const delPassword = async (pin: string, name: string) => {
  await $fetch(`${getBaseUrl()}/del?name=${name}`, {
    method: "DELETE",
    headers: { Authorization: pin },
  }).catch(() => undefined);
};
